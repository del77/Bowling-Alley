package pl.lodz.p.it.ssbd2019.ssbd03.mot.service;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Reservation;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.Score;

import javax.annotation.security.RolesAllowed;
import java.util.List;

public class ReservationServiceImpl implements ReservationService {
    @Override
    @RolesAllowed("GetAlleyGamesHistory")
    public List<Reservation> getFinishedReservationsForAlley(Long alleyId) {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed("EnterGameResult")
    public void enterScoresForReservation(Long reservationId, List<Score> reservationScores) {
        throw new UnsupportedOperationException();
    }
}
