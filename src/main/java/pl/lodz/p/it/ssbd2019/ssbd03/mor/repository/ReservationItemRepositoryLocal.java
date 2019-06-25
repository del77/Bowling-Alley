package pl.lodz.p.it.ssbd2019.ssbd03.mor.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.ReservationItem;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.ReservationItemId;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.DataAccessException;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.CruRepository;

import javax.ejb.Local;
import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.util.List;

@Local
public interface ReservationItemRepositoryLocal extends CruRepository<ReservationItem, ReservationItemId>  {
    
    /** Pobiera przedmioty zerezerwowane w podanej rezerwacji
     *
     * @param reservationId identyfikator rezerwacji
     * @return lista zarezerwowanych przedmiotów
     * @throws DataAccessException W razie błędu związanego z połączeniem z bazą
     */
    List<ReservationItem> getItemsForReservation(Long reservationId) throws DataAccessException;
    
    /**
     * Pobiera z bazy wszystkie przedmioty zarezerwowane w rezerwacji w podanym okresie
     *
     * @param startTime początek okresu
     * @param endTime koniec okresu
     * @return lista encji zarezerwowanych przedmiotów
     * @throws DataAccessException W przypadku błedu połączenia z bazą
     */
    List<ReservationItem> getReservationItemsFromReservationsWithinTimeFrame(Timestamp startTime, Timestamp endTime) throws DataAccessException;
    
    /**
     * Usuwa z bazy zarezerwowany przedmiot
     *
     * @param item encja przedmiotu
     * @throws DataAccessException nieznaleziony przedmiot lub błąd połączenia z bazą
     */
    void delete(ReservationItem item) throws DataAccessException;
}
