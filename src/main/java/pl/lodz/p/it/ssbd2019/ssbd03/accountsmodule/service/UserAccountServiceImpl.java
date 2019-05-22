package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service;

import org.hibernate.Hibernate;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository.AccessLevelRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository.UserAccountRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccessLevel;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccountAccessLevel;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.*;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.UniqueConstraintViolationHandler;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.localization.LocalizedMessageProvider;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.messaging.ClassicMessage;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.messaging.Messenger;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MokRoles;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.SHA256Provider;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.tracker.InterceptorTracker;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.tracker.TransactionTracker;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.*;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.ejb.EJBTransactionRolledbackException;
import javax.xml.registry.infomodel.User;
import java.util.List;

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
    public List<UserAccount> getAllUsers() throws EntityRetrievalException {
        try {
            return userAccountRepositoryLocal.findAll();
        } catch (Exception e) {
            throw new EntityRetrievalException("Could not retrieve users list", e);
        }
    }

    @Override
    @RolesAllowed(MokRoles.GET_USER_DETAILS)
    public UserAccount getUserById(Long id) throws EntityRetrievalException {
        try {
            UserAccount user = userAccountRepositoryLocal.findById(id).orElseThrow(
                    () -> new EntityRetrievalException("No such userAccount with given id"));
            Hibernate.initialize(user.getAccountAccessLevels());
            return user;
        } catch (Exception e) {
            throw new EntityRetrievalException("Could not retrieve userAccount by id", e);
        }
    }


    @Override
    @RolesAllowed({MokRoles.EDIT_USER_ACCOUNT, MokRoles.EDIT_OWN_ACCOUNT})
    public UserAccount updateUser(UserAccount userAccount) throws EntityUpdateException, NotUniqueEmailException {
        try {
            return userAccountRepositoryLocal.edit(userAccount);
        } catch (EntityUpdateException e) {
            throw new EntityUpdateException("Data is not up-to-date", e);
        } catch (EJBTransactionRolledbackException e) {
            UniqueConstraintViolationHandler.handleNotUniqueEmailException(e, EntityUpdateException.class);
            throw new EntityUpdateException("Unknown error", e);
        } catch (Exception e) {
            throw new EntityUpdateException("Could not update userAccount", e);
        }
    }

    @Override
    @RolesAllowed({MokRoles.CHANGE_ACCESS_LEVEL, MokRoles.EDIT_OWN_ACCOUNT})
    public UserAccount updateUserAccessLevels(UserAccount userAccount, List<String> selectedAccessLevels) throws EntityUpdateException {
        try {
            setActiveFieldForExistingAccountAccessLevelsOfEditedUser(userAccount.getAccountAccessLevels(), selectedAccessLevels);
            addNewAccountAccessLevelsForEditedUser(userAccount, selectedAccessLevels);

            return userAccountRepositoryLocal.edit(userAccount);
        } catch (EntityUpdateException e) {
            throw new EntityUpdateException("Data is not up-to-date", e);
        } catch (Exception e) {
            throw new EntityUpdateException("Could not update userAccount", e);
        }
    }

    @Override
    @PermitAll
    public UserAccount getByLogin(String login) throws EntityRetrievalException {
        try {
            UserAccount user = userAccountRepositoryLocal.findByLogin(login).orElseThrow(
                    () -> new EntityRetrievalException("No account with login specified."));
            Hibernate.initialize(user.getAccountAccessLevels());
            return user;
        } catch (Exception e) {
            throw new EntityRetrievalException("Could not retrieve user with specified login.", e);
        }
    }

    @Override
    @RolesAllowed(MokRoles.CHANGE_OWN_PASSWORD)
    public void changePasswordByLogin(String login, String currentPassword, String newPassword) throws ChangePasswordException {
        try {
            UserAccount account = this.getByLogin(login);
            String currentPasswordHash = SHA256Provider.encode(currentPassword);

            if (!currentPasswordHash.equals(account.getPassword())) {
                throw new ChangePasswordException("Current password is incorrect.");
            }

            setNewPassword(account, newPassword);
        } catch (Exception e) {
            throw new ChangePasswordException(e.getMessage());
        }
    }

    @Override
    @RolesAllowed(MokRoles.CHANGE_USER_PASSWORD)
    public void changePasswordById(long id, String newPassword) throws ChangePasswordException {
        try {
            UserAccount account = this.getUserById(id);
            setNewPassword(account, newPassword);
        } catch (Exception e) {
            throw new ChangePasswordException(e.getMessage());
        }
    }

    @Override
    @RolesAllowed(MokRoles.LOCK_UNLOCK_ACCOUNT)
    public UserAccount updateLockStatusOnAccountById(Long id, boolean isActive) throws EntityUpdateException {
        try {
            UserAccount account = getUserById(id);
            account.setAccountActive(isActive);
            UserAccount editedAccount = userAccountRepositoryLocal.edit(account);
            sendMessageAboutActiveChanged(editedAccount);
            return editedAccount;
        } catch (Exception e) {
            throw new EntityUpdateException("Could not unlock user", e);
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
     * @throws EntityUpdateException w wypadku, gdy nie uda się aktualizacja.
     */
    private void addNewAccountAccessLevelsForEditedUser(UserAccount userAccount, List<String> selectedAccessLevels) throws EntityUpdateException {
        for (String selectedAccessLevel : selectedAccessLevels) {
            AccessLevel accessLevel = accessLevelRepositoryLocal.findByName(selectedAccessLevel).orElseThrow(
                    () -> new EntityUpdateException("Assigned AccessLevel does not exist."));
            userAccount.getAccountAccessLevels().add(AccountAccessLevel.builder()
                    .account(userAccount)
                    .accessLevel(accessLevel)
                    .active(true)
                    .build()
            );
        }
    }

    /**
     * Zmienia hasło dla konta.
     *
     * @param userAccount Obiekt typu UserAccount, który jest edytowany.
     * @param newPassword Nowe hasło dla konta.
     * @throws ChangePasswordException w wypadku, gdy nie uda się zmienić hasła.
     */
    private void setNewPassword(UserAccount userAccount, String newPassword) throws ChangePasswordException {
        try {
            String newPasswordHash = SHA256Provider.encode(newPassword);
            userAccount.setPassword(newPasswordHash);
            userAccountRepositoryLocal.edit(userAccount);
        } catch (EntityUpdateException e) {
            throw new ChangePasswordException("Couldn't change the password because the account was changed by other user.", e);
        } catch (Exception e) {
            throw new ChangePasswordException(e.getMessage());
        }
    }

    private void sendMessageAboutActiveChanged(UserAccount account) {
        ClassicMessage message = ClassicMessage
                .builder()
                .subject(localization.get("bowlingAlley") + " - " + localization.get("accountStatusChanged"))
                .to(account.getEmail())
                .from("ssbd201903@gmail.com")
                .body(account.isAccountActive() ? localization.get("yourAccountUnlocked") : localization.get("yourAccountLocked"))
                .build();

        try {
            messenger.sendMessage(message);
        } catch (MessageNotSentException ignored) {

        }
    }
}
