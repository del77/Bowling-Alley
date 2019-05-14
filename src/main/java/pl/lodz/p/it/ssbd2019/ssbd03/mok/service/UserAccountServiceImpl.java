package pl.lodz.p.it.ssbd2019.ssbd03.mok.service;

import org.hibernate.Hibernate;
import pl.lodz.p.it.ssbd2019.ssbd03.mok.repository.AccessLevelRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.mok.repository.UserAccountRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccessLevel;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccountAccessLevel;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.ChangePasswordException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityRetrievalException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityUpdateException;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.SHA256Provider;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.List;

@Stateless
@Transactional
public class UserAccountServiceImpl implements UserAccountService {
    @EJB(beanName = "MOKUserRepository")
    UserAccountRepositoryLocal userAccountRepositoryLocal;

    @EJB(beanName = "MOKAccessLevelRepository")
    AccessLevelRepositoryLocal accessLevelRepositoryLocal;

    @Override
    @RolesAllowed("GetAllUsersList")
    public List<UserAccount> getAllUsers() throws EntityRetrievalException {
        try {
            return userAccountRepositoryLocal.findAll();
        } catch (Exception e) {
            throw new EntityRetrievalException("Could not retrieve users list", e);
        }
    }

    @Override
    @RolesAllowed("GetUserDetails")
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
    @RolesAllowed({"ChangeAccessLevel", "EditUserAccount", "EditOwnAccount"})
    public UserAccount updateUserWithAccessLevels(UserAccount userAccount, List<String> selectedAccessLevels) throws EntityUpdateException {
        try {
            setActiveFieldForExistingAccountAccessLevelsOfEditedUser(userAccount.getAccountAccessLevels(), selectedAccessLevels);
            addNewAccountAccessLevelsForEditedUser(userAccount,selectedAccessLevels);

            return userAccountRepositoryLocal.edit(userAccount);
        } catch (Exception e) {
            throw new EntityUpdateException("Could not update userAccount", e);
        }
    }

    @Override
    @RolesAllowed({"ChangeOwnPassword", "GetOwnAccountDetails"})
    public UserAccount getByLogin(String login) throws EntityRetrievalException {
        try {
            return userAccountRepositoryLocal.findByLogin(login).orElseThrow(
                    () -> new EntityRetrievalException("No account with login specified."));
        } catch (Exception e) {
            throw new EntityRetrievalException("Could not retrieve user with specified login.", e);
        }
    }

    @Override
    @RolesAllowed("ChangeOwnPassword")
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
    @RolesAllowed("ChangeUserPassword")
    public void changePasswordById(long id, String newPassword) throws ChangePasswordException {
        try {
            UserAccount account = this.getUserById(id);
            setNewPassword(account, newPassword);
        } catch (Exception e) {
            throw new ChangePasswordException(e.getMessage());
        }
    }
    
    @Override
    @RolesAllowed("LockUnlockAccount")
    public UserAccount updateLockStatusOnAccountById(Long id, boolean isActive) throws EntityUpdateException {
        try {
            UserAccount account = getUserById(id);
            account.setAccountActive(isActive);
            return userAccountRepositoryLocal.edit(account);
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
     * @param userAccount Obiekt typu UserAccount, który jest edytowany.
     * @param selectedAccessLevels Obiekt typu List<String>, który reprezentuje zaznaczone przy edycji poziomy dostępu
     * @throws EntityUpdateException w wypadku, gdy nie uda się aktualizacja.
     */
    private void addNewAccountAccessLevelsForEditedUser(UserAccount userAccount, List<String> selectedAccessLevels) throws EntityUpdateException{
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
     * @param userAccount Obiekt typu UserAccount, który jest edytowany.
     * @param newPassword Nowe hasło dla konta.
     * @throws ChangePasswordException w wypadku, gdy nie uda się zmienić hasła.
     */
    private void setNewPassword(UserAccount userAccount, String newPassword) throws ChangePasswordException {
        try {
            String newPasswordHash = SHA256Provider.encode(newPassword);
            userAccount.setPassword(newPasswordHash);
        } catch (Exception e) {
            throw new ChangePasswordException(e.getMessage());
        }
    }
}
