package pl.lodz.p.it.ssbd2019.ssbd03.mok.service;

import org.hibernate.Hibernate;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.*;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.conflict.AccountAlreadyConfirmedException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.conflict.AccountPasswordNotUniqueException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.AccessLevelDoesNotExistException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.EntityRetrievalException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.LoginDoesNotExistException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.UserIdDoesNotExistException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.generalized.ChangePasswordException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.notfound.TokenNotFoundException;
import pl.lodz.p.it.ssbd2019.ssbd03.mok.repository.AccessLevelRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.mok.repository.ConfirmationTokenRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.mok.repository.UserAccountRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.mok.web.dto.AccountDetailsDto;
import pl.lodz.p.it.ssbd2019.ssbd03.mok.web.dto.UserRolesDto;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.SHA256Provider;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.helpers.Mapper;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.helpers.UserAccountHelpers;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.localization.LocalizedMessageProvider;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.messaging.Messenger;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.AppRolesProvider;
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
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.Context;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors(InterceptorTracker.class)
public class UserAccountServiceImpl extends TransactionTracker implements UserAccountService {
    @EJB(beanName = "MOKUserRepository")
    UserAccountRepositoryLocal userAccountRepositoryLocal;

    @EJB(beanName = "MOKAccessLevelRepository")
    AccessLevelRepositoryLocal accessLevelRepositoryLocal;

    @EJB(beanName = "MOKConfirmationTokenRepository")
    private ConfirmationTokenRepositoryLocal confirmationTokenRepository;

    @EJB
    private Messenger messenger;

    @Inject
    private LocalizedMessageProvider localization;

    @Inject
    private AppRolesProvider appRolesProvider;

    private UserAccount userAccount;

    @Context
    private HttpServletRequest httpServletRequest;

    @Override
    @RolesAllowed(MokRoles.GET_ALL_USERS_LIST)
    public List<AccountDetailsDto> getAllUsers() throws SsbdApplicationException {
        List<UserAccount> allAccounts = userAccountRepositoryLocal.findAll();
        List<AccountDetailsDto> allUsers = new ArrayList<>();
        for (UserAccount account : allAccounts) {
            allUsers.add(Mapper.map(account, AccountDetailsDto.class));
        }
        return allUsers;
    }

    @Override
    @RolesAllowed(MokRoles.GET_USER_DETAILS)
    public AccountDetailsDto getUserById(Long id) throws SsbdApplicationException {
        this.userAccount = userAccountRepositoryLocal.findById(id).orElseThrow(
                () -> new UserIdDoesNotExistException("Account with id '" + id + "' does not exist."));

        AccountDetailsDto accountDetailsDto = Mapper.map(this.userAccount, AccountDetailsDto.class);
        retrieveRolesFromUserAccount(userAccount, accountDetailsDto);
        return accountDetailsDto;
    }


    @Override
    @RolesAllowed({MokRoles.EDIT_USER_ACCOUNT, MokRoles.EDIT_OWN_ACCOUNT})
    public void updateUser(AccountDetailsDto userAccountDto) throws SsbdApplicationException {
        this.userAccount.setFirstName(userAccountDto.getFirstName());
        this.userAccount.setLastName(userAccountDto.getLastName());
        this.userAccount.setEmail(userAccountDto.getEmail());
        this.userAccount.setPhone(userAccountDto.getPhone());
        userAccountRepositoryLocal.edit(this.userAccount);
    }

    @Override
    @RolesAllowed({MokRoles.CHANGE_ACCESS_LEVEL, MokRoles.EDIT_OWN_ACCOUNT})
    public void updateUserAccessLevels(UserRolesDto userAccountDto, List<String> selectedAccessLevels) throws SsbdApplicationException {
        setActiveFieldForExistingAccountAccessLevelsOfEditedUser(this.userAccount.getAccountAccessLevels(), selectedAccessLevels);
        addNewAccountAccessLevelsForEditedUser(this.userAccount, selectedAccessLevels);
        userAccountRepositoryLocal.edit(this.userAccount);
    }

    @Override
    @PermitAll
    public AccountDetailsDto getByLogin(String login) throws SsbdApplicationException {
        this.userAccount = userAccountRepositoryLocal.findByLogin(login).orElseThrow(
                () -> new LoginDoesNotExistException("Account with login '" + login + "' does not exist."));
        AccountDetailsDto account = Mapper.map(this.userAccount, AccountDetailsDto.class);
        retrieveRolesFromUserAccount(userAccount, account);

        return account;
    }

    @Override
    @RolesAllowed(MokRoles.CHANGE_OWN_PASSWORD)
    public void changePasswordByLogin(String login, String currentPassword, String newPassword) throws SsbdApplicationException {
        UserAccount user = userAccountRepositoryLocal.findByLogin(login).orElseThrow(
                () -> new LoginDoesNotExistException("Account with login '" + login + "' does not exist."));
        String currentPasswordHash = SHA256Provider.encode(currentPassword);

        if (!currentPasswordHash.equals(user.getPassword())) {
            throw new ChangePasswordException("Current password is incorrect.");
        }

        setNewPassword(user, newPassword);
    }

    @Override
    @RolesAllowed(MokRoles.CHANGE_USER_PASSWORD)
    public void changePasswordById(long id, String newPassword) throws SsbdApplicationException {
        UserAccount user = userAccountRepositoryLocal.findById(id).orElseThrow(
                () -> new UserIdDoesNotExistException("Account with id '" + id + "' does not exist."));
        setNewPassword(user, newPassword);
    }

    @Override
    @RolesAllowed(MokRoles.LOCK_UNLOCK_ACCOUNT)
    public void updateLockStatusOnAccountById(Long id, boolean isActive) throws SsbdApplicationException {
        UserAccount account = userAccountRepositoryLocal.findById(id).orElseThrow(
                () -> new UserIdDoesNotExistException("Account with id '" + id + "' does not exist."));
        account.setAccountActive(isActive);
        userAccountRepositoryLocal.editWithoutMerge(account);

        messenger.sendMessage(
                account.getEmail(),
                localization.get("bowlingAlley") + " - " + localization.get("accountStatusChanged"),
                account.isAccountActive() ? localization.get("yourAccountUnlocked") : localization.get("yourAccountLocked")
        );
    }

    @Override
    @PermitAll
    public List<AccountDetailsDto> getAllByNameOrLastName(String name) {
        List<UserAccount> users = userAccountRepositoryLocal.findAllByNameOrLastName(name);
        for (UserAccount user : users) {
            Hibernate.initialize(user.getAccountAccessLevels());
        }
        List<AccountDetailsDto> allUsers = new ArrayList<>();
        for (UserAccount user : users) {

            allUsers.add(Mapper.map(user, AccountDetailsDto.class));
        }
        return allUsers;
    }

    @Override
    public void activateAccountByToken(String token) throws SsbdApplicationException {
        ConfirmationToken tokenEntity = confirmationTokenRepository
                .findByToken(token)
                .orElseThrow(() -> new TokenNotFoundException("Couldn't find token with provided value."));
        @NotNull UserAccount userAccount = tokenEntity.getUserAccount();
        if (UserAccountHelpers.isUserAccountConfirmed(userAccount, appRolesProvider.getUnconfirmed())) {
            throw new AccountAlreadyConfirmedException("Account already confirmed.");
        }
        deactivateUnconfirmedAccessLevel(userAccount);
        activateClientAccessLevel(userAccount);
        confirmationTokenRepository.edit(tokenEntity);

        messenger.sendMessage(
                userAccount.getEmail(),
                localization.get("bowlingAlley") + " - " + localization.get("accountStatusChanged"),
                localization.get("accountConfirmed")
        );
    }

    private void retrieveRolesFromUserAccount(UserAccount userAccount, AccountDetailsDto userAccountDto) {
        for(AccountAccessLevel accountAccessLevel : userAccount.getAccountAccessLevels()) {
            if(accountAccessLevel.isActive()) {
                if (accountAccessLevel.getAccessLevel().getName().equals(appRolesProvider.getClient())) {
                    userAccountDto.setClientRoleSelected(true);
                } else if (accountAccessLevel.getAccessLevel().getName().equals(appRolesProvider.getEmployee())) {
                    userAccountDto.setEmployeeRoleSelected(true);
                } else if (accountAccessLevel.getAccessLevel().getName().equals(appRolesProvider.getAdmin())) {
                    userAccountDto.setAdminRoleSelected(true);
                } else if (accountAccessLevel.getAccessLevel().getName().equals(appRolesProvider.getUnconfirmed())) {
                    userAccountDto.setConfirmed(false);
                }
            }
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
     *
     * @param newPassword Nowe hasło dla konta.
     * @throws SsbdApplicationException w wypadku, gdy nie uda się zmienić hasła.
     */
    private void setNewPassword(UserAccount user, String newPassword) throws SsbdApplicationException {
        String newPasswordHash = SHA256Provider.encode(newPassword);

        if (isNewPasswordUniqueForUser(user, newPasswordHash)) {
            addCurrentPasswordToHistory(user);
            user.setPassword(newPasswordHash);
        } else {
            throw new AccountPasswordNotUniqueException("New password was used before.");
        }
        userAccountRepositoryLocal.edit(user);
    }

    /**
     * Sprawdza czy nowe hasło nie było wcześniej używane przez użytkownika
     *
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
     *
     * @param userAccount obiekt konta użytkownika
     */
    private void addCurrentPasswordToHistory(UserAccount userAccount) {
        PreviousUserPassword newPrevious = PreviousUserPassword.builder()
                .password(userAccount.getPassword())
                .build();
        userAccount.getPreviousUserPasswords().add(newPrevious);
    }

    /**
     * Metoda deaktywująca poziom dostępu oznaczający niepotwierdzone konto.
     *
     * @param userAccount Obiekt konta użytkownika.
     */
    private void deactivateUnconfirmedAccessLevel(UserAccount userAccount) {
        userAccount.getAccountAccessLevels().stream()
                .filter(x -> x.getAccessLevel().getName().equals(appRolesProvider.getUnconfirmed()))
                .findFirst().get()
                .setActive(false);
    }

    /**
     * Metoda aktywująca dla konta poziom dostępu "klient"
     *
     * @param userAccount obiekt konta użytkownika
     */
    private void activateClientAccessLevel(UserAccount userAccount) {
        AccountAccessLevel clientAccountAccessLevel = userAccount.getAccountAccessLevels().stream()
                .filter(x -> x.getAccessLevel().getName().equals(appRolesProvider.getClient()))
                .findFirst()
                .orElse(null);

        if(clientAccountAccessLevel == null) {
            AccessLevel clientAccessLevel = accessLevelRepositoryLocal.findByName(appRolesProvider.getClient()).get();
            clientAccountAccessLevel = AccountAccessLevel.builder()
                    .accessLevel(clientAccessLevel)
                    .account(userAccount)
                    .active(true)
                    .build();
            userAccount.getAccountAccessLevels().add(clientAccountAccessLevel);
        } else {
            clientAccountAccessLevel.setActive(true);
        }
    }
}
