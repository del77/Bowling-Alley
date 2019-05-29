package pl.lodz.p.it.ssbd2019.ssbd03.mor.service;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Comment;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.Reservation;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MorRoles;

import javax.annotation.security.RolesAllowed;
import java.util.List;

public class ReservationServiceImpl implements ReservationService {
    @Override
    @RolesAllowed({MorRoles.CREATE_RESERVATION, MorRoles.CREATE_RESERVATION_FOR_USER})
    public void addReservation(Reservation reservation, String userLogin) {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed({MorRoles.EDIT_OWN_RESERVATION, MorRoles.EDIT_RESERVATION_FOR_USER})
    public void updateReservation(Reservation reservation) {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed({MorRoles.CANCEL_OWN_RESERVATION, MorRoles.CANCEL_RESERVATION_FOR_USER})
    public void cancelReservation(Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed({MorRoles.GET_RESERVATIONS_FOR_USER, MorRoles.GET_OWN_RESERVATIONS})
    public List<Reservation> getReservationsForUser(String userLogin) {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed(MorRoles.GET_RESERVATIONS_FOR_ALLEY)
    public List<Reservation> getReservationsForAlley(Long alleyId) {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed(MorRoles.GET_RESERVATION_DETAILS)
    public List<Reservation> getReservationsById(Long id) {
        throw new UnsupportedOperationException();
    }


    @Override
    @RolesAllowed(MorRoles.ADD_COMMENT_FOR_RESERVATION)
    public void addCommentForReservation(Long id, Comment comment) {
        throw new UnsupportedOperationException();
    }
}
