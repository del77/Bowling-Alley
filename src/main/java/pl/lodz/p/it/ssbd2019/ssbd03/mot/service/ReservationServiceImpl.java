package pl.lodz.p.it.ssbd2019.ssbd03.mot.service;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Reservation;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.Score;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.DataAccessException;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.repository.ReservationRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.web.dto.ReservationFullDto;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.helpers.Mapper;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MotRoles;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.List;

@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Stateful(name = "MOTReservationService")
@DenyAll
public class ReservationServiceImpl implements ReservationService {

    @EJB(beanName = "MOTReservationRepository")
    private ReservationRepositoryLocal reservationRepository;

    @Override
    @RolesAllowed({MotRoles.GET_ALLEY_GAMES_HISTORY})
    public List<ReservationFullDto> getFinishedReservationsForAlley(Long alleyId) throws SsbdApplicationException {
        List<Reservation> reservations = reservationRepository.findFinishedReservationsForAlley(alleyId);
        return Mapper.mapAll(reservations,ReservationFullDto.class);
    }

    @Override
    @RolesAllowed(MotRoles.ENTER_GAME_RESULT)
    public void enterScoresForReservation(Long reservationId, List<Score> reservationScores) {
        throw new UnsupportedOperationException();
    }
}
