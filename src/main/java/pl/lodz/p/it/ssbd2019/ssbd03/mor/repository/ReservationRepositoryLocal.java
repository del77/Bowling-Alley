package pl.lodz.p.it.ssbd2019.ssbd03.mor.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Reservation;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.DataAccessException;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.CruRepository;

import javax.ejb.Local;
import java.util.List;

@Local
public interface ReservationRepositoryLocal extends CruRepository<Reservation, Long> {

    /**
     * Zwraca rezerwacje dla zadanego użytkownika
     *
     * @param userId id użytkownika
     * @return lista rezerwacji
     * @throws DataAccessException błąd w dostępie do danych
     */
    List<Reservation> findReservationsForUser(Long userId) throws DataAccessException;

    /**
     * Zwraca rezerwacje dla zadanego toru
     *
     * @param alleyId id toru
     * @return lista rezerwacji
     * @throws DataAccessException błąd w dostępie do danych
     */
    List<Reservation> findReservationsForAlley(Long alleyId) throws DataAccessException;
}
