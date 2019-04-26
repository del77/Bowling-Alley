package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service;

import org.hibernate.Hibernate;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository.UserRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.EditUserDto;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccountAccessLevel;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.User;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityCreationException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityRetrievalException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityUpdateException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.List;

@Stateless
@Transactional
public class UserServiceImpl implements UserService {
    @EJB(beanName = "MOKUserRepository")
    UserRepositoryLocal userRepositoryLocal;

    @EJB
    private AccountAccessLevelService accountAccessLevelService;

    @Override
    public List<User> getAllUsers() throws EntityRetrievalException {
        try {
            return userRepositoryLocal.findAll();
        } catch (Exception e) {
            throw new EntityRetrievalException("Could not retrieve users list", e);
        }
    }

    @Override
    public User getUserById(Long id) throws EntityRetrievalException {
        try {
            User user = userRepositoryLocal.findById(id).orElseThrow(
                    () -> new EntityRetrievalException("No such user with given id"));
            Hibernate.initialize(user.getAccount().getAccountAccessLevels());
            return user;
        } catch (Exception e) {
            throw new EntityRetrievalException("Could not retrieve user by id", e);
        }
    }

    @Override
    public User addUser(User user) throws EntityCreationException {
        try {
            return userRepositoryLocal.create(user);
        } catch (Exception e) {
            throw new EntityCreationException("Could not add user", e);
        }
    }

    @Override
    public User updateUser(User user) throws EntityUpdateException {
        try {
            return userRepositoryLocal.edit(user);
        } catch (Exception e) {
            throw new EntityUpdateException("Could not update user", e);
        }
    }

    @Override
    public User updateUser(EditUserDto user) throws EntityUpdateException {
        try {
            User existingUser = userRepositoryLocal.findById(user.getId()).orElseThrow(
                    () -> new EntityRetrievalException("No such user with given id")
            );
            //
            // update existing user here
            //
            accountAccessLevelService.updateAccountAccessLevels(existingUser.getId(), "CLIENT", user.isClientRole());
            accountAccessLevelService.updateAccountAccessLevels(existingUser.getId(), "EMPLOYEE", user.isEmployeeRole());
            accountAccessLevelService.updateAccountAccessLevels(existingUser.getId(), "ADMIN", user.isAdminRole());
            return userRepositoryLocal.edit(existingUser);
        } catch (Exception e) {
            throw new EntityUpdateException("Could not update user", e);
        }
    }
}
