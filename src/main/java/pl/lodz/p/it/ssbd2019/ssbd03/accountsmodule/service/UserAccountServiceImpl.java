package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service;

import org.hibernate.Hibernate;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository.AccessLevelRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository.UserAccountRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccessLevel;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccountAccessLevel;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.PreviousUserPassword;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.conflict.AccountPasswordNotUniqueException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.*;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.generalized.ChangePasswordException;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.SHA256Provider;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.localization.LocalizedMessageProvider;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.messaging.Messenger;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MokRoles;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.tracker.InterceptorTracker;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.tracker.TransactionTracker;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.util.List;
import java.util.stream.Collectors;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Interceptors(InterceptorTracker.class)
public class UserAccountServiceImpl extends TransactionTracker implements UserAccountService {
    @EJB(beanName = "MOKUserRepository")
    UserAccountRepositoryLocal userAccountRepositoryLocal;

    @EJB(beanName = "MOKAccessLevelRepository")
    AccessLevelRepositoryLocal accessLevelRepositoryLocal;

    @EJB
    private Messenger messenger;

    @Inject
    private LocalizedMessageProvider localization;

    @Override
    @RolesAllowed(MokRoles.GET_ALL_USERS_LIST)
    public List<UserAccount> getAllUsers() throws SsbdApplicationException {
        return userAccountRepositoryLocal.findAll();
    }

    @Override
    @RolesAllowed(MokRoles.GET_USER_DETAILS)
    public UserAccount getUserById(Long id) throws SsbdApplicationException {
        UserAccount user = userAccountRepositoryLocal.findById(id).orElseThrow(
                () -> new UserIdDoesNotExistException("Account with id '" + id + "' does not exist."));
        Hibernate.initialize(user.getAccountAccessLevels());
        return user;
    }


    @Override
    @RolesAllowed({MokRoles.EDIT_USER_ACCOUNT, MokRoles.EDIT_OWN_ACCOUNT})
    public UserAccount updateUser(UserAccount userAccount) throws SsbdApplicationException {
        return userAccountRepositoryLocal.edit(userAccount);
    }

    @Override
    @RolesAllowed({MokRoles.CHANGE_ACCESS_LEVEL, MokRoles.EDIT_OWN_ACCOUNT})
    public UserAccount updateUserAccessLevels(UserAccount userAccount, List<String> selectedAccessLevels) throws SsbdApplicationException {
        setActiveFieldForExistingAccountAccessLevelsOfEditedUser(userAccount.getAccountAccessLevels(), selectedAccessLevels);
        addNewAccountAccessLevelsForEditedUser(userAccount, selectedAccessLevels);

        return userAccountRepositoryLocal.edit(userAccount);
    }

    @Override
    @PermitAll
    public UserAccount getByLogin(String login) throws SsbdApplicationException {
        UserAccount user = userAccountRepositoryLocal.findByLogin(login).orElseThrow(
                () -> new LoginDoesNotExistException("Account with login '" + login + "' does not exist."));
        Hibernate.initialize(user.getAccountAccessLevels());
        return user;
    }

    @Override
    @RolesAllowed(MokRoles.CHANGE_OWN_PASSWORD)
    public void changePasswordByLogin(String login, String currentPassword, String newPassword) throws SsbdApplicationException {
        UserAccount account = this.getByLogin(login);
        String currentPasswordHash = SHA256Provider.encode(currentPassword);

        if (!currentPasswordHash.equals(account.getPassword())) {
            throw new ChangePasswordException("Current password is incorrect.");
        }

        setNewPassword(account, newPassword);
    }

    @Override
    @RolesAllowed(MokRoles.CHANGE_USER_PASSWORD)
    public void changePasswordById(long id, String newPassword) throws SsbdApplicationException {
        UserAccount account = this.getUserById(id);
        setNewPassword(account, newPassword);
    }

    @Override
    @RolesAllowed(MokRoles.LOCK_UNLOCK_ACCOUNT)
    public UserAccount updateLockStatusOnAccountById(Long id, boolean isActive) throws SsbdApplicationException {
        UserAccount account = getUserById(id);
        account.setAccountActive(isActive);
        UserAccount editedAccount = userAccountRepositoryLocal.editWithoutMerge(account);

        messenger.sendMessage(
                account.getEmail(),
                localization.get("bowlingAlley") + " - " + localization.get("accountStatusChanged"),
                account.isAccountActive() ? localization.get("yourAccountUnlocked") : localization.get("yourAccountLocked")
        );

        return editedAccount;
    }
    
    @PermitAll
    public void restartFailedLoginsCounter(String login) throws SsbdApplicationException {
        UserAccount account = getByLogin(login);
        account.setFailedLoginsCounter(0);
        userAccountRepositoryLocal.editWithoutMerge(account);
    }
    
    @PermitAll
    public void incrementFailedLoginsCounter(String login) throws SsbdApplicationException {
        UserAccount account = getByLogin(login);
        Integer counter = account.getFailedLoginsCounter();
        if (counter == null) {
            account.setFailedLoginsCounter(1);
            userAccountRepositoryLocal.editWithoutMerge(account);
        } else if (counter < 2) {
            account.setFailedLoginsCounter(++counter);
            userAccountRepositoryLocal.editWithoutMerge(account);
        } else {
            account.setFailedLoginsCounter(0); // reset it now, so after unlocking the account back it won't be locked after 1 failed attempt
            account.setAccountActive(false);
            userAccountRepositoryLocal.editWithoutMerge(account); // edit before sending email in case this method throws an exception
            messenger.sendMessage(
                    account.getEmail(),
                    localization.get("bowlingAlley") + " - " + localization.get("accountStatusChanged"),
                    localization.get("accountLockedByFailedLogins")
            );
        }
    }

    /**
     * Ustawia prawidłowy stan dla flagi active
     * w istniejących dla użytkownika poziomach dostępu.
     */
    private void setActiveFieldForExistingAccountAccessLevelsOfEditedUser(List<AccountAccessLevel> accountAccessLevels,
                                                                          List<String> selectedAccessLevels) {
        for (AccountAccessLevel accountAccessLevel : accountAccessLevels) {
            if (selectedAccessLevels.contains(accountAccessLevel.getAccessLevel().getName())) {
                accountAccessLevel.setActive(true);
                selectedAccessLevels.remove(accountAccessLevel.getAccessLevel().getName());
            } else {
                accountAccessLevel.setActive(false);
            }
        }
    }

    /**
     * Dodaje dla użytkownika poziomy dostępu, które nie były dla niego wcześniej przydzielone.
     *
     * @param userAccount          Obiekt typu UserAccount, który jest edytowany.
     * @param selectedAccessLevels Obiekt typu List<String>, który reprezentuje zaznaczone przy edycji poziomy dostępu
     * @throws EntityRetrievalException w wypadku, gdy nie uda się pobrac poziomu dostępu.
     */
    private void addNewAccountAccessLevelsForEditedUser(UserAccount userAccount, List<String> selectedAccessLevels) throws EntityRetrievalException {
        for (String selectedAccessLevel : selectedAccessLevels) {
            AccessLevel accessLevel = accessLevelRepositoryLocal.findByName(selectedAccessLevel).orElseThrow(
                    () -> new AccessLevelDoesNotExistException("AccessLevel '" + selectedAccessLevel + "' does not exist."));
            userAccount.getAccountAccessLevels().add(AccountAccessLevel.builder()
                    .account(userAccount)
                    .accessLevel(accessLevel)
                    .active(true)
                    .build()
            );
        }
    }

    /**
     * Dopisuje aktualne hasło do historii haseł użytkownika i zmienia je.
     * @param userAccount Obiekt typu UserAccount, który jest edytowany.
     * @param newPassword Nowe hasło dla konta.
     * @throws ChangePasswordException w wypadku, gdy nie uda się zmienić hasła.
     */
    private void setNewPassword(UserAccount userAccount, String newPassword) throws SsbdApplicationException {
        String newPasswordHash = SHA256Provider.encode(newPassword);

        if(isNewPasswordUniqueForUser(userAccount, newPasswordHash)) {
            addCurrentPasswordToHistory(userAccount);
            userAccount.setPassword(newPasswordHash);
        } else {
            throw new AccountPasswordNotUniqueException("New password was used before.");
        }
        userAccountRepositoryLocal.edit(userAccount);
    }

    /**
     * Sprawdza czy nowe hasło nie było wcześniej używane przez użytkownika
     * @param userAccount użytkownik dla którego sprawdzana jest unikalność hasla
     * @param newPassword nowe hasło
     * @return rezultat sprawdzenia
     */
    private boolean isNewPasswordUniqueForUser(UserAccount userAccount, String newPassword) {
        List<String> previousPasswords = userAccount.getPreviousUserPasswords().stream()
                .map(PreviousUserPassword::getPassword).collect(Collectors.toList());

        return !previousPasswords.contains(newPassword) && !userAccount.getPassword().equals(newPassword);
    }

    /**
     * Dodaje istniejące hasło użytkownika do historii haseł
     * @param userAccount obiekt konta użytkownika
     */
    private void addCurrentPasswordToHistory(UserAccount userAccount) {
        PreviousUserPassword newPrevious = PreviousUserPassword.builder()
                .password(userAccount.getPassword())
                .build();
        userAccount.getPreviousUserPasswords().add(newPrevious);
    }
}
