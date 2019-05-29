package pl.lodz.p.it.ssbd2019.ssbd03.mot.service;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Reservation;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.Score;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MotRoles;

import javax.annotation.security.RolesAllowed;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.List;

@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class ReservationServiceImpl implements ReservationService {
    @Override
    @RolesAllowed(MotRoles.GET_ALLEY_GAMES_HISTORY)
    public List<Reservation> getFinishedReservationsForAlley(Long alleyId) {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed(MotRoles.ENTER_GAME_RESULT)
    public void enterScoresForReservation(Long reservationId, List<Score> reservationScores) {
        throw new UnsupportedOperationException();
    }
}
