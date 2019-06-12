package pl.lodz.p.it.ssbd2019.ssbd03.mor.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Reservation;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.DataAccessException;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.CruRepository;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MorRoles;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.List;
import java.util.Optional;

@Local
public interface ReservationRepositoryLocal extends CruRepository<Reservation, Long> {

    @RolesAllowed({MorRoles.GET_RESERVATION_DETAILS, MorRoles.EDIT_RESERVATION_FOR_USER})
    List<Reservation> findReservationsForUser(Long userId) throws DataAccessException;

}
