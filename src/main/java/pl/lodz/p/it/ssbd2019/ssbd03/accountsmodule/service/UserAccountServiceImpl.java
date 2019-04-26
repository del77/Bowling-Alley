package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service;

import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository.UserAccountRepositoryLocal;
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
            return userAccountRepositoryLocal.findById(id).orElseThrow(
                    () -> new EntityRetrievalException("No such userAccount with given id")
            );
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
    public UserAccount updateUser(UserAccount userAccount) throws EntityUpdateException {
        try {
            return userAccountRepositoryLocal.edit(userAccount);
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
