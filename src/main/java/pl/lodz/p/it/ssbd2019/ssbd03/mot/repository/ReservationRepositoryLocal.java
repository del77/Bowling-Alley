package pl.lodz.p.it.ssbd2019.ssbd03.mot.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Reservation;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.DataAccessException;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.CruRepository;

import javax.ejb.Local;
import java.util.List;

@Local
public interface ReservationRepositoryLocal extends CruRepository<Reservation, Long> {

    /**
     * Zwraca zakończone rezerwacje dla danego toru.
     *
     * @param alleyId id toru
     * @return lista rezerwacji
     * @throws SsbdApplicationException błąd w dostępie do danych
     */
    List<Reservation> findFinishedReservationsForAlley(Long alleyId) throws SsbdApplicationException;
}
