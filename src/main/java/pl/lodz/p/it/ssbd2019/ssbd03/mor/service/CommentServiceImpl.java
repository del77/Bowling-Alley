package pl.lodz.p.it.ssbd2019.ssbd03.mor.service;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Comment;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.Reservation;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.DataAccessException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.conflict.CommentNotOwnedReservationException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.ReservationDoesNotExistException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.generalized.CommentAddingException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.generalized.ReservationMustBeCompletedException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.notfound.NotFoundException;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.repository.CommentRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.repository.ReservationRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.repository.UserAccountRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.CommentDto;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.CommentEditDto;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.helpers.Mapper;
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

    @EJB(beanName = "MORUserAccountRepository")
    UserAccountRepositoryLocal userAccountRepositoryLocal;

    private Comment comment;

    @Override
    @RolesAllowed(MorRoles.EDIT_COMMENT_FOR_OWN_RESERVATION)
    public void editComment(CommentEditDto comment, String login) throws DataAccessException {
        if (canUserEditComment(this.comment, login)) {
            this.comment.setContent(comment.getContent());
            commentRepositoryLocal.edit(this.comment);
        }
    }

    @Override
    @RolesAllowed(MorRoles.EDIT_COMMENT_FOR_OWN_RESERVATION)
    public CommentDto getActiveCommentForUserById(Long commentId, String login) throws DataAccessException, NotFoundException {
        Comment comment = commentRepositoryLocal.findById(commentId).orElseThrow(NotFoundException::new);

        if (!canUserEditComment(comment, login)) {
            throw new NotFoundException();
        }

        this.comment = comment;
        return Mapper.map(comment, CommentDto.class);
    }

    @Override
    @RolesAllowed(MorRoles.ADD_COMMENT_FOR_RESERVATION)
    public void addComment(Long reservationId, CommentDto commentDto, String userLogin) throws SsbdApplicationException {
        Optional<Reservation> reservationById = reservationRepositoryLocal.findById(reservationId);
        Reservation reservation = reservationById.orElseThrow(ReservationDoesNotExistException::new);
        checkIfUserOwnsReservation(reservation, userLogin);
        checkReservationExpired(reservation);
        Comment serviceRequest = Comment
                .builder()
                .active(true)
                .content(commentDto.getContent())
                .date(Timestamp.from(Instant.now()))
                .reservation(reservation)
                .build();
        commentRepositoryLocal.create(serviceRequest);
    }

    private void checkIfUserOwnsReservation(Reservation reservation, String userLogin) throws CommentAddingException {
        if (!reservation.getUserAccount().getLogin().equals(userLogin)) {
            throw new CommentNotOwnedReservationException();
        }
    }

    private void checkReservationExpired(Reservation reservation) throws ReservationMustBeCompletedException {
        if (!ReservationValidator.isExpired(reservation.getStartDate())) {
            throw new ReservationMustBeCompletedException("Ones can only add comments to completed reservations");
        }
    }

    /**
     * @param comment komentarz
     * @param login   login użytkownika
     * @return wartość logiczną czy użytkownik może edytować komentarz
     */
    private boolean canUserEditComment(Comment comment, String login) {
        boolean isCommentActive = comment.isActive();
        boolean isUserOwner = comment.getReservation().getUserAccount().getLogin().equals(login);

        return isCommentActive && isUserOwner;
    }
}
