package pl.lodz.p.it.ssbd2019.ssbd03.mor.service;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Alley;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.Comment;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.Reservation;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.conflict.ReservationAlreadyInactiveException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.conflict.StateConflictedException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.AlleyDoesNotExistException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.DataAccessException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.LoginDoesNotExistException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.ReservationDoesNotExistException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.generalized.AlleyNotAvailableException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.generalized.CreateRegistrationException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.generalized.DataParseException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.notfound.NotFoundException;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.repository.AlleyRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.repository.ReservationRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.repository.UserAccountRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.AvailableAlleyDto;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.ClientNewReservationDto;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.ReservationFullDto;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.helpers.Mapper;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.helpers.StringToTimestampConverter;
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
import java.util.List;
import java.util.stream.Collectors;

@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Stateful(name = "MORReservationService")
@Interceptors(InterceptorTracker.class)
@DenyAll
public class ReservationServiceImpl extends TransactionTracker implements ReservationService {

    @EJB(beanName = "MORReservationRepository")
    private ReservationRepositoryLocal reservationRepositoryLocal;

    @EJB(beanName = "MORAlleyRepository")
    private AlleyRepositoryLocal alleyRepositoryLocal;

    @EJB(beanName = "MORUserAccountRepository")
    private UserAccountRepositoryLocal userAccountRepositoryLocal;

    private Reservation reservation;

    @Override
    @RolesAllowed({MorRoles.CREATE_RESERVATION, MorRoles.CREATE_RESERVATION_FOR_USER})
    public List<AvailableAlleyDto> getAvailableAlleysInTimeRange(ClientNewReservationDto newReservationDto) throws SsbdApplicationException {
        Timestamp startTime = StringToTimestampConverter.getStartDate(newReservationDto).orElseThrow(DataParseException::new);
        Timestamp endTime = StringToTimestampConverter.getEndDate(newReservationDto).orElseThrow(DataParseException::new);
        List<Alley> alleys = alleyRepositoryLocal.getAvailableAlleysInTimeRange(startTime, endTime);
        return Mapper.mapAll(alleys, AvailableAlleyDto.class);
    }

    @Override
    @RolesAllowed({MorRoles.CREATE_RESERVATION, MorRoles.CREATE_RESERVATION_FOR_USER})
    public void addReservation(ClientNewReservationDto newReservationDto, Long alleyId, String userLogin) throws SsbdApplicationException {
        Timestamp startTime = StringToTimestampConverter.getStartDate(newReservationDto).orElseThrow(DataParseException::new);
        Timestamp endTime = StringToTimestampConverter.getEndDate(newReservationDto).orElseThrow(DataParseException::new);
        if (!alleyRepositoryLocal.isAvailableAlleyInTimeRange(startTime, endTime, alleyId)) {
            throw new AlleyNotAvailableException();
        }

        Alley alley = alleyRepositoryLocal.findById(alleyId).orElseThrow(AlleyDoesNotExistException::new);
        UserAccount userAccount = userAccountRepositoryLocal.findByLogin(userLogin).orElseThrow(LoginDoesNotExistException::new);
        Reservation newReservation = Reservation.builder()
                .userAccount(userAccount)
                .startDate(startTime)
                .endDate(endTime)
                .playersCount(newReservationDto.getNumberOfPlayers())
                .active(true)
                .alley(alley)
                .build();
        try {
            reservationRepositoryLocal.create(newReservation);
        } catch (DataAccessException e) {
            throw e;
        } catch (Exception e) {
            throw new CreateRegistrationException(e.getMessage());
        }
    }

    @Override
    @RolesAllowed({MorRoles.EDIT_OWN_RESERVATION, MorRoles.EDIT_RESERVATION_FOR_USER})
    public void updateReservation(Reservation reservation) {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed({MorRoles.CANCEL_OWN_RESERVATION, MorRoles.CANCEL_RESERVATION_FOR_USER})
    public void cancelReservation(Long id) throws DataAccessException, StateConflictedException {
        if(!reservation.isActive()){
            throw new ReservationAlreadyInactiveException();
        }
        reservation.setActive(false);
        reservationRepositoryLocal.edit(reservation);
    }

    @Override
    @RolesAllowed({MorRoles.GET_RESERVATIONS_FOR_USER, MorRoles.GET_OWN_RESERVATIONS})
    public List<ReservationFullDto> getReservationsForUser(Long userId) throws DataAccessException {
        return reservationRepositoryLocal
                .findReservationsForUser(userId)
                .stream()
                .map(reservation -> Mapper.map(reservation, ReservationFullDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @RolesAllowed(MorRoles.GET_OWN_RESERVATIONS)
    public List<ReservationFullDto> getReservationsByUserLogin(String login) throws DataAccessException {
        List<Reservation> reservations = userAccountRepositoryLocal.findByLogin(login)
                .orElseThrow(LoginDoesNotExistException::new)
                .getReservations();
        return Mapper.mapAll(reservations, ReservationFullDto.class);
    }

    @Override
    @RolesAllowed(MorRoles.GET_RESERVATIONS_FOR_ALLEY)
    public List<ReservationFullDto> getReservationsForAlley(Long alleyId) throws DataAccessException {
        List<Reservation> reservations = reservationRepositoryLocal.findReservationsForAlley(alleyId);
        return Mapper.mapAll(reservations, ReservationFullDto.class);
    }

    @Override
    @RolesAllowed({MorRoles.GET_RESERVATION_DETAILS})
    public ReservationFullDto getReservationById(Long id) throws DataAccessException {
        reservation = reservationRepositoryLocal.findById(id)
                .orElseThrow(ReservationDoesNotExistException::new);
        return Mapper.map(reservation, ReservationFullDto.class);
    }

    @Override
    @RolesAllowed({MorRoles.GET_OWN_RESERVATION_DETAILS})
    public ReservationFullDto getUserReservationById(Long id, String login) throws DataAccessException {
        Reservation reservation = reservationRepositoryLocal.findById(id).orElseThrow(ReservationDoesNotExistException::new);

        if (!reservation.getUserAccount().getLogin().equals(login)) {
            throw new ReservationDoesNotExistException();
        }

        this.reservation = reservation;
        return Mapper.map(reservation, ReservationFullDto.class);
    }

    @Override
    @RolesAllowed(MorRoles.DISABLE_COMMENT)
    public void disableComment(Long id) throws SsbdApplicationException {
        Comment comment = reservation.getComments().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);

        if(comment == null) {
            throw new NotFoundException("There is no comment with id: " + id);
        }
        comment.setActive(false);
        reservationRepositoryLocal.edit(reservation);
    }
}
