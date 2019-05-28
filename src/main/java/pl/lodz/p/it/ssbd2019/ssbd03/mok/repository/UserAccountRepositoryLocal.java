package pl.lodz.p.it.ssbd2019.ssbd03.mok.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.DataAccessException;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.CruRepository;

import javax.ejb.Local;
import java.util.List;
import java.util.Optional;

@Local
public interface UserAccountRepositoryLocal extends CruRepository<UserAccount, Long> {

    /**
     * Metoda służy do pozyskiwania encji konta użytkownika na podstawie jego loginu.
     *
     * @param login Login użytkownika
     * @throws DataAccessException gdy nie uda się pobrać encji konta użytkownika
     * @return Encja reprezentująca konto użytkownika.
     */
    Optional<UserAccount> findByLogin(String login) throws DataAccessException;

    /**
     * Metoda służy do pozyskiwania encji konta użytkownika na podstawie jego adresu email.
     *
     * @param email Login użytkownika
     * @return Encja reprezentująca konto użytkownika.
     */
    Optional<UserAccount> findByEmail(String email);

    /**
     * Metoda służy do pozyskiwania listy encji kont użytkowników zawierających podany ciąg znaków w imieniu lub nazwisku
     *
     * @param name Część imienia bądź nazwiska użytkowników, których chcemy wyszukać.
     * @return Lista encji reprezentujących konta użytkowników.
     */
    List<UserAccount> findAllByNameOrLastName(String name);

}
