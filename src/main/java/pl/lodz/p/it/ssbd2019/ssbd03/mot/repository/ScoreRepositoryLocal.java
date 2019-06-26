package pl.lodz.p.it.ssbd2019.ssbd03.mot.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Score;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.DataAccessException;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.CruRepository;

import javax.ejb.Local;
import java.util.List;

@Local
public interface ScoreRepositoryLocal extends CruRepository<Score, Long> {

    /**
     * Zwraca wyniki dla rezerwacji o podanym ID
     *
     * @param id ID rezerwacji
     * @return lista wyników
     * @throws SsbdApplicationException błąd w dostępie do danych
     */
    List<Score> getScoresByReservation(Long id) throws SsbdApplicationException;

    /**
     * Zwraca wyniki dla toru o podanym ID
     *
     * @param id ID rezerwacji
     * @return lista wyników
     * @throws SsbdApplicationException błąd w dostępie do danych
     */
    List<Score> getScoresByAlley(Long id) throws SsbdApplicationException;
}
