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
     * Zwraca tory, które nie są zarezerwowane dla zadanego okresu pomijając jedną z nich.
     * @param startTime czas początku przedziału
     * @param endTime czas końca przedziału
     * @param reservationId identyfikator rezerwacji, która ma nie być brana pod uwagę
     * @return tory
     * @throws DataAccessException Nieudana kwerenda
     */
    List<Alley> getAvailableAlleysInTimeRangeExcludingReservation(Timestamp startTime, Timestamp endTime, Long reservationId) throws DataAccessException;
    
    /**
     * Zwraca tor o podanym numerze
     *
     * @param number numer toru
     * @return encja toru o podanym numerze
     * @throws DataAccessException Brak rekordu toru o podanym numerze
     */
    Optional<Alley> findByNumber(int number) throws DataAccessException;
}