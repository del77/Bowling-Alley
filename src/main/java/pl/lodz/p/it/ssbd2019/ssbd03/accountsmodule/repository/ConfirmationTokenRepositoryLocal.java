package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.ConfirmationToken;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.CruRepository;

import javax.ejb.Local;
import java.util.Optional;

/**
 * Repozytorium dla encji typu ConfirmationToken.
 */
@Local
public interface ConfirmationTokenRepositoryLocal extends CruRepository<ConfirmationToken, Long> {

    /**
     * Metoda służy do pozyskiwania encji tokenu potwierdzenia na podstawie jego wartości.
     * @param token wartość tokena UUID w postaci String
     * @return Encja reprezentująca token potwierdzający.
     */
    Optional<ConfirmationToken> findByToken(String token);
}
