package pl.lodz.p.it.ssbd2019.ssbd03.mor.service;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Comment;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.Reservation;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.generalized.ReservationMustBeCompletedException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.notfound.NotFoundException;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.repository.CommentRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.repository.ReservationRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.CommentDto;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.helpers.ReservationValidator;
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
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Stateful(name = "MORCommentService")
@Interceptors(InterceptorTracker.class)
@DenyAll
public class CommentServiceImpl extends TransactionTracker implements CommentService {
    @EJB(beanName = "MORReservationRepository")
    ReservationRepositoryLocal reservationRepositoryLocal;

    @EJB(beanName = "MORCommentRepository")
    CommentRepositoryLocal commentRepositoryLocal;

    @Override
    @RolesAllowed(MorRoles.EDIT_COMMENT_FOR_RESERVATION)
    public void editComment(Comment comment) {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed(MorRoles.ADD_COMMENT_FOR_RESERVATION)
    public void addComment(Long reservationId, CommentDto commentDto) throws SsbdApplicationException {
        Optional<Reservation> reservationById = reservationRepositoryLocal.findById(reservationId);
        Reservation reservation = reservationById.orElseThrow(NotFoundException::new);
        if (!ReservationValidator.isExpired(reservation.getStartDate())) {
            throw new ReservationMustBeCompletedException("Ones can only add comments to completed reservations");
        }
        Comment serviceRequest = Comment
                .builder()
                .active(true)
                .content(commentDto.getContent())
                .date(Timestamp.from(Instant.now()))
                .reservation(reservation)
                .build();
        commentRepositoryLocal.create(serviceRequest);
    }
}
