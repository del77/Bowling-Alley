package pl.lodz.p.it.ssbd2019.ssbd03.mor.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.CruRepository;

import javax.ejb.Local;
import java.util.Optional;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.EntityRetrievalException;

@Local
public interface UserAccountRepositoryLocal extends CruRepository<UserAccount, Long> {
    /**
     * Metoda służy do pozyskiwania encji konta użytkownika na podstawie jego loginu.
     * @param login Login użytkownika
     * @return Encja reprezentująca konto użytkownika.
     */
    Optional<UserAccount> findByLogin(String login) throws EntityRetrievalException;
}
