package pl.lodz.p.it.ssbd2019.ssbd03.mor.service;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Comment;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.Reservation;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.DataAccessException;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.repository.ReservationRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.ReservationDto;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.helpers.Mapper;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MorRoles;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.tracker.InterceptorTracker;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.tracker.TransactionTracker;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Stateful
@Interceptors(InterceptorTracker.class)
@DenyAll
public class ReservationServiceImpl extends TransactionTracker implements ReservationService {

    @EJB(beanName = "MORReservationRepository")
    private ReservationRepositoryLocal reservationRepositoryLocal;

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
    public List<ReservationDto> getReservationsForUser(Long userId) throws DataAccessException {
        return reservationRepositoryLocal
                .findReservationsForUser(userId)
                .stream()
                .map(reservation -> Mapper.map(reservation, ReservationDto.class))
                .collect(Collectors.toList());
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
