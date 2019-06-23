package pl.lodz.p.it.ssbd2019.ssbd03.mor.service;

import lombok.AllArgsConstructor;
import org.javatuples.Pair;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.Alley;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.Comment;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.Reservation;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.conflict.AlleyAlreadyReservedException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.conflict.ReservationAlreadyInactiveException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.conflict.StateConflictedException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.*;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.generalized.CreateRegistrationException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.generalized.DataParseException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.generalized.NotYourReservationException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.notfound.AlleyNotFoundException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.notfound.NotFoundException;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.repository.AlleyRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.repository.ReservationRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.repository.UserAccountRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.AvailableAlleyDto;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.DetailedReservationDto;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.NewReservationDto;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.ReservationFullDto;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.helpers.Mapper;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.helpers.StringTimestampConverter;
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
import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Stateful(name = "MORReservationService")
@Interceptors(InterceptorTracker.class)
@DenyAll
public class ReservationServiceImpl extends TransactionTracker implements ReservationService {
    
    private Reservation ownEditedReservation;

    @EJB(beanName = "MORReservationRepository")
    private ReservationRepositoryLocal reservationRepositoryLocal;

    @EJB(beanName = "MORAlleyRepository")
    private AlleyRepositoryLocal alleyRepositoryLocal;

    @EJB(beanName = "MORUserAccountRepository")
    private UserAccountRepositoryLocal userAccountRepositoryLocal;

    private Reservation reservation;

    @Override
    @RolesAllowed({MorRoles.CREATE_RESERVATION, MorRoles.CREATE_RESERVATION_FOR_USER, MorRoles.EDIT_OWN_RESERVATION, MorRoles.EDIT_RESERVATION_FOR_USER})
    public List<AvailableAlleyDto> getAvailableAlleysInTimeRange(NewReservationDto newReservationDto) throws SsbdApplicationException {
        Timestamp startTime = StringTimestampConverter.getStartDate(newReservationDto).orElseThrow(DataParseException::new);
        Timestamp endTime = StringTimestampConverter.getEndDate(newReservationDto).orElseThrow(DataParseException::new);
        List<Alley> alleys = getAvailableAlleysInTimeRange(startTime, endTime);
        return Mapper.mapAll(alleys, AvailableAlleyDto.class);
    }

    @Override
    @RolesAllowed({MorRoles.CREATE_RESERVATION, MorRoles.CREATE_RESERVATION_FOR_USER})
    public void addReservation(NewReservationDto newReservationDto, Long alleyId, String userLogin) throws SsbdApplicationException {
        Alley alley = alleyRepositoryLocal.findById(alleyId).orElseThrow(AlleyDoesNotExistException::new);
        UserAccount userAccount = userAccountRepositoryLocal.findByLogin(userLogin).orElseThrow(LoginDoesNotExistException::new);
        Timestamp startTime = StringTimestampConverter.getStartDate(newReservationDto).orElseThrow(DataParseException::new);
        Timestamp endTime = StringTimestampConverter.getEndDate(newReservationDto).orElseThrow(DataParseException::new);
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
    public DetailedReservationDto updateReservation(DetailedReservationDto reservationDto, String userLogin) throws SsbdApplicationException {
        if (!doesReservationBelongToUser(this.ownEditedReservation, userLogin)) {
            throw new NotYourReservationException();
        }
        
        Timestamp startDate = StringTimestampConverter.getTimestamp(
                reservationDto.getStartDay(),
                reservationDto.getStartHour()).orElseThrow(DataParseException::new
        );
        Timestamp endDate = StringTimestampConverter.getTimestamp(
                reservationDto.getEndDay(),
                reservationDto.getEndHour()).orElseThrow(DataParseException::new
        );
    
        Alley alley = alleyRepositoryLocal.findByNumber(
                reservationDto.getAlleyNumber())
                .orElseThrow(AlleyNotFoundException::new);
        
        if (!isAlleyAvailable(alley.getNumber(), startDate, endDate)) {
            throw new AlleyAlreadyReservedException();
        }
        
        ownEditedReservation.setAlley(alley);
        ownEditedReservation.setStartDate(startDate);
        ownEditedReservation.setEndDate(endDate);
        ownEditedReservation.setPlayersCount(reservationDto.getNumberOfPlayers());
        reservationRepositoryLocal.edit(ownEditedReservation);
        return reservationDto;
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
                .map(res -> Mapper.map(res, ReservationFullDto.class))
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
    @RolesAllowed({MorRoles.GET_OWN_RESERVATION_DETAILS, MorRoles.EDIT_OWN_RESERVATION})
    public DetailedReservationDto getOwnReservationById(Long reservationId, String userLogin) throws SsbdApplicationException {
        Reservation res = reservationRepositoryLocal.findById(reservationId).orElseThrow(EntityNotFoundException::new);
        if (doesReservationBelongToUser(res, userLogin)) {
            this.ownEditedReservation = res;
        } else {
            throw new NotYourReservationException();
        }
        Pair<Optional<String>, Optional<String>> startDateTime = StringTimestampConverter.getDateAndTimeStrings(this.ownEditedReservation.getStartDate());
        Pair<Optional<String>, Optional<String>> endDateTime = StringTimestampConverter.getDateAndTimeStrings(this.ownEditedReservation.getEndDate());

        return DetailedReservationDto.builder()
                .startDay(startDateTime.getValue0().orElse(""))
                .startHour(startDateTime.getValue1().orElse(""))
                .endDay(endDateTime.getValue0().orElse(""))
                .endHour(endDateTime.getValue1().orElse(""))
                .active(this.ownEditedReservation.isActive())
                .numberOfPlayers(this.ownEditedReservation.getPlayersCount())
                .alleyNumber(this.ownEditedReservation.getAlley().getNumber())
                .build();
    }


    @Override
    @RolesAllowed({MorRoles.GET_OWN_RESERVATION_DETAILS})
    public ReservationFullDto getUserReservationById(Long id, String login) throws DataAccessException {
        Reservation res = reservationRepositoryLocal.findById(id).orElseThrow(ReservationDoesNotExistException::new);

        if (!res.getUserAccount().getLogin().equals(login)) {
            throw new ReservationDoesNotExistException();
        }

        this.reservation = res;
        return Mapper.map(res, ReservationFullDto.class);
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
    
    private boolean doesReservationBelongToUser(Reservation reservation, String userLogin) {
        try {
            return reservation.getUserAccount().getLogin().equals(userLogin);
        } catch (NullPointerException e) {
            return false;
        }
    }
    
    private List<Alley> getAvailableAlleysInTimeRange(Timestamp start, Timestamp end) throws DataAccessException {
        return alleyRepositoryLocal.getAvailableAlleysInTimeRange(start, end);
    }
    
    private boolean isAlleyAvailable(int number, Timestamp startDate, Timestamp endDate) throws DataAccessException {
        return getAvailableAlleysInTimeRange(startDate, endDate)
                .stream()
                .map(Alley::getNumber)
                .collect(Collectors.toList())
                .contains(number);
    }
}
