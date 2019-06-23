package pl.lodz.p.it.ssbd2019.ssbd03.mor.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.ReservationItem;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.ReservationItemId;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.DataAccessException;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.CruRepository;

import javax.ejb.Local;
import java.util.List;

@Local
public interface ReservationItemRepositoryLocal extends CruRepository<ReservationItem, ReservationItemId>  {
    
    /** Pobiera przedmioty zerezerwowane w podanej rezerwacji
     *
     * @param reservationId identyfikator rezerwacji
     * @return lista zarezerwowanych przedmiotów
     * @throws DataAccessException W razie błędu związanego z połączeniem z bazą
     */
    public List<ReservationItem> getItemsForReservation(Long reservationId) throws DataAccessException;
}
