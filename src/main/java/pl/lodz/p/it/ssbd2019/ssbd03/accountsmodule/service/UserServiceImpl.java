package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service;

import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository.UserRepositoryLocal;
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
            return userRepositoryLocal.findById(id).orElseThrow(
                    () -> new EntityRetrievalException("No such user with given id")
            );
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
            throw new EntityUpdateException("Could not update", e);
        }
    }
}
