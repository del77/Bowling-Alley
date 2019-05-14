package pl.lodz.p.it.ssbd2019.ssbd03.mor.service;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Comment;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.Reservation;

import javax.annotation.security.RolesAllowed;
import java.util.List;

public class ReservationServiceImpl implements ReservationService {
    @Override
    @RolesAllowed({"CreateReservation", "CreateReservationForUser"})
    public void addReservation(Reservation reservation, String userLogin) {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed({"EditOwnReservation", "EditReservationForUser"})
    public void updateReservation(Reservation reservation) {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed({"CancelOwnReservation", "CancelReservationForUser"})
    public void cancelReservation(Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed("GetReservationsForUser")
    public List<Reservation> getReservationsForUser(String userLogin) {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed("GetReservationsForAlley")
    public List<Reservation> getReservationsForAlley(Long alleyId) {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed("GetReservationDetails")
    public List<Reservation> getReservationsById(Long Id) {
        throw new UnsupportedOperationException();
    }


    @Override
    @RolesAllowed("AddCommentForReservation")
    public void addCommentForReservation(Long id, Comment comment) {
        throw new UnsupportedOperationException();
    }
}
