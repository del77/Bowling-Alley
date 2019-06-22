package pl.lodz.p.it.ssbd2019.ssbd03.mor.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Alley;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.DataAccessException;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.CruRepository;

import javax.ejb.Local;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Local
public interface AlleyRepositoryLocal extends CruRepository<Alley, Long> {

    /**
     * Zwraca tory, które nie są zarezerwowane dla zadanego przedziału czasu.
     * @param startTime czas początku przedziału
     * @param endTime czas końca przedziału
     * @return tory
     * @throws DataAccessException Nieudana kwerenda
     */
    List<Alley> getAvailableAlleysInTimeRange(Timestamp startTime, Timestamp endTime) throws DataAccessException;
    
    /**
     * Looks for an alley with a given number
     *
     * @param number alley's number
     * @return entity with given number
     */
    Optional<Alley> findByNumber(int number);
}