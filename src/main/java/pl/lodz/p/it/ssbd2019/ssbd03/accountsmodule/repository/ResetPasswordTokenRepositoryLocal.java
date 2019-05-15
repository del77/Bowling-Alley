package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.ResetPasswordToken;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.CruRepository;

import javax.ejb.Local;
import java.util.Optional;

@Local
public interface ResetPasswordTokenRepositoryLocal extends CruRepository<ResetPasswordToken, Long> {
    /**
     * Metoda służy do pozyskiwania encji tokenu na podstawie tokenu
     *
     * @param token Token
     * @return Encja reprezentująca token.
     */
    Optional<ResetPasswordToken> findByToken(String token);
}
