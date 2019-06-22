package pl.lodz.p.it.ssbd2019.ssbd03.mor.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Alley;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.EntityRetrievalException;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.CruRepository;

import javax.ejb.Local;
import java.sql.Timestamp;
import java.util.List;

@Local
public interface AlleyRepositoryLocal extends CruRepository<Alley, Long> {

    /**
     * Zwraca tory, które nie są zarezerwowane dla zadanego przedziału czasu.
     * @param startTime czas początku przedziału
     * @param endTime czas końca przedziału
     * @return tory
     * @throws EntityRetrievalException
     */
    List<Alley> getAvailableAlleysInTimeRange(Timestamp startTime, Timestamp endTime) throws EntityRetrievalException;

    /**
     * Zwraca tory, które nie są zarezerwowane dla zadanego przedziału czasu.
     * @param startTime czas początku przedziału
     * @param endTime czas końca przedziału
     * @param alleyId identyfikator toru
     * @return true lub false
     * @throws EntityRetrievalException
     */
    boolean isAvailableAlleyInTimeRange(Timestamp startTime, Timestamp endTime, Long alleyId) throws EntityRetrievalException;
}