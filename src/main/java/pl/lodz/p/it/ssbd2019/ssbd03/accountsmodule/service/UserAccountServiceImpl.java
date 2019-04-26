package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service;

import org.hibernate.Hibernate;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository.AccessLevelRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository.UserAccountRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.AccessVersionDto;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccessLevel;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccountAccessLevel;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.ChangePasswordException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityCreationException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityRetrievalException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityUpdateException;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.SHA256Provider;

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
    public List<UserAccount> getAllUsers() throws EntityRetrievalException {
        try {
            return userAccountRepositoryLocal.findAll();
        } catch (Exception e) {
            throw new EntityRetrievalException("Could not retrieve users list", e);
        }
    }

    @Override
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
    public UserAccount addUser(UserAccount userAccount) throws EntityCreationException {
        try {
            return userAccountRepositoryLocal.create(userAccount);
        } catch (Exception e) {
            throw new EntityCreationException("Could not add userAccount", e);
        }
    }

    @Override
    public UserAccount updateUser(UserAccount userAccount, List<AccessVersionDto> selectedAccessLevels) throws EntityUpdateException {
        try {
            UserAccount existingUser = userAccountRepositoryLocal.findById(userAccount.getId()).orElseThrow(
                    () -> new EntityUpdateException("Updated user does not exist."));

            existingUser.setVersion(userAccount.getVersion());
            //todo: set remaining existingUser fields here...
//            for(AccountAccessLevel aal : existingUser.getAccountAccessLevels()) {
//                AccountAccessLevel
//                if(aal.isActive() && selectedAccessLevels.contains(aal.getAccessLevel().getName()) == false) {
//                    aal.setActive(false);
//                }
//            }
            for(AccessVersionDto accessLevel : selectedAccessLevels) {
                AccessLevel al = accessLevelRepositoryLocal.findByName(accessLevel.getName()).orElseThrow(
                        () -> new EntityUpdateException("Assigned AccessLevel does not exist."));
                AccountAccessLevel aal = existingUser.getAccountAccessLevels().stream()
                        .filter((accountAccessLevel) -> accountAccessLevel.getAccessLevel().equals(al))
                        .findFirst()
                        .orElse(null);
                if(aal == null) {
                    if(accessLevel.isSelected()) {
                        existingUser.getAccountAccessLevels().add(AccountAccessLevel.builder()
                                .account(existingUser).accessLevel(al).active(true).build());
                    }
                }
                else {
                    aal.setActive(accessLevel.isSelected());
                    aal.setVersion(accessLevel.getVersion());
                }
            }
            return userAccountRepositoryLocal.edit(existingUser);
        } catch (Exception e) {
            throw new EntityUpdateException("Could not update userAccount", e);
        }
    }

    @Override
    public UserAccount getByLogin(String login) throws EntityRetrievalException {
        try {
            return userAccountRepositoryLocal.findByLogin(login).orElseThrow(
                    () -> new EntityRetrievalException("No account with login specified."));
        } catch (EntityRetrievalException e) {
            throw new EntityRetrievalException("Could not retrieve user with specified login.", e);
        }
    }

    @Override
    public UserAccount changePassword(String login, String currentPassword, String newPassword) throws ChangePasswordException {
        try {
            UserAccount account = this.getByLogin(login);
            String currentPasswordHash = SHA256Provider.encode(currentPassword);
            String newPasswordHash = SHA256Provider.encode(newPassword);

            if (!currentPasswordHash.equals(account.getPassword())) {
                throw new ChangePasswordException("Current password is incorrect.");
            }

            account.setPassword(newPasswordHash);
            return userAccountRepositoryLocal.edit(account);
        } catch (Exception e) {
            throw new ChangePasswordException(e.getMessage());
        }
    }
}
