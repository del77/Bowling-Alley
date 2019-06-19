package pl.lodz.p.it.ssbd2019.ssbd03.mot.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.DataAccessException;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.CruRepository;

import java.util.Optional;

public interface UserAccountRepositoryLocal extends CruRepository<UserAccount, Long> {

    /**
     * Metoda służy do pozyskiwania encji konta użytkownika na podstawie jego loginu.
     *
     * @param login Login użytkownika
     * @return Encja reprezentująca konto użytkownika.
     * @throws DataAccessException gdy nie uda się pobrać encji konta użytkownika
     */
    Optional<UserAccount> findByLogin(String login) throws DataAccessException;
}
