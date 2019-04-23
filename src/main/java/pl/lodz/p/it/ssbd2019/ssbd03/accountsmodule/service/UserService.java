package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.User;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityCreationException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityRetrievalException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityUpdateException;

import java.util.List;

/**
 * Klasa reprezentująca logikę biznesową dla operacji związanych z obiektami oraz encjami klasy User.
 */
public interface UserService {

    List<User> getAllUsers() throws EntityRetrievalException;
    User getUserById(Long id) throws EntityRetrievalException;
    User addUser(User user) throws EntityCreationException;
    User updateUser(User user) throws EntityUpdateException;

}
