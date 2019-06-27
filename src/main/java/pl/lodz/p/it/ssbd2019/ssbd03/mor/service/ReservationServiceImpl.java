package pl.lodz.p.it.ssbd2019.ssbd03.mor.service;

import org.javatuples.Pair;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.*;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.conflict.*;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.*;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.generalized.AlleyNotAvailableException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.generalized.CreateRegistrationException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.generalized.DataParseException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.generalized.NotYourReservationException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.notfound.AlleyNotFoundException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.notfound.NotFoundException;

import pl.lodz.p.it.ssbd2019.ssbd03.mor.repository.AlleyRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.repository.ReservationRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.repository.UserAccountRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.AvailableAlleyDto;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.new_reservation.BallsDto;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.new_reservation.ClientNewReservationDto;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.ReservationFullDto;

import pl.lodz.p.it.ssbd2019.ssbd03.mor.repository.*;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.*;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.new_reservation.ShoesDto;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.helpers.Mapper;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.helpers.ReservationValidator;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.helpers.StringTimestampConverter;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.localization.LocalizedMessageProvider;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.messaging.Messenger;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.messaging.mail.EmailMessenger;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MorRoles;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.tracker.InterceptorTracker;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.tracker.TransactionTracker;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Stateful(name = "MORReservationService")
@Interceptors(InterceptorTracker.class)
@DenyAll
public class ReservationServiceImpl extends TransactionTracker implements ReservationService {

    private Reservation ownEditedReservation;
    private Reservation userEditedReservation;

    @EJB(beanName = "MORReservationRepository")
    private ReservationRepositoryLocal reservationRepositoryLocal;

    @EJB(beanName = "MORReservationItemRepository")
    private ReservationItemRepositoryLocal reservationItemRepositoryLocal;

    @EJB(beanName = "MORItemRepository")
    private ItemRepositoryLocal itemRepositoryLocal;

    @EJB(beanName = "MORAlleyRepository")
    private AlleyRepositoryLocal alleyRepositoryLocal;

    @EJB(beanName = "MORUserAccountRepository")
    private UserAccountRepositoryLocal userAccountRepositoryLocal;

    @EJB
    private Messenger messenger;

    @Inject
    private LocalizedMessageProvider localization;

    private Reservation reservation;

    @Override
    @RolesAllowed({MorRoles.CREATE_RESERVATION, MorRoles.CREATE_RESERVATION_FOR_USER})
    public List<AvailableAlleyDto> getAvailableAlleysInTimeRange(ClientNewReservationDto newReservationDto) throws SsbdApplicationException {
        Timestamp startTime = StringTimestampConverter.getStartDate(newReservationDto).orElseThrow(DataParseException::new);
        Timestamp endTime = StringTimestampConverter.getEndDate(newReservationDto).orElseThrow(DataParseException::new);
        List<Alley> alleys = alleyRepositoryLocal.getAvailableAlleysInTimeRange(startTime, endTime);
        return Mapper.mapAll(alleys, AvailableAlleyDto.class);
    }

    @Override
    @RolesAllowed({MorRoles.CREATE_RESERVATION, MorRoles.CREATE_RESERVATION_FOR_USER, MorRoles.EDIT_OWN_RESERVATION, MorRoles.EDIT_RESERVATION_FOR_USER})
    public List<AvailableAlleyDto> getAvailableAlleysInTimeRange(Timestamp start, Timestamp end) throws SsbdApplicationException {
        List<Alley> alleys = alleyRepositoryLocal.getAvailableAlleysInTimeRange(start, end);
        return Mapper.mapAll(alleys, AvailableAlleyDto.class);
    }

    @Override
    @RolesAllowed(MorRoles.EDIT_OWN_RESERVATION)
    public List<AvailableAlleyDto> getAvailableAlleysInTimeRangeExcludingOwnReservation(Timestamp start, Timestamp end) throws SsbdApplicationException {
        List<Alley> alleys = alleyRepositoryLocal.getAvailableAlleysInTimeRangeExcludingReservation(start, end, this.ownEditedReservation.getId());
        return Mapper.mapAll(alleys, AvailableAlleyDto.class);
    }

    @Override
    @RolesAllowed(MorRoles.EDIT_RESERVATION_FOR_USER)
    public List<AvailableAlleyDto> getAvailableAlleysInTimeRangeExcludingUserReservation(Timestamp start, Timestamp end) throws SsbdApplicationException {
        List<Alley> alleys = alleyRepositoryLocal.getAvailableAlleysInTimeRangeExcludingReservation(start, end, this.userEditedReservation.getId());
        return Mapper.mapAll(alleys, AvailableAlleyDto.class);
    }

    @Override
    @RolesAllowed({MorRoles.CREATE_RESERVATION, MorRoles.CREATE_RESERVATION_FOR_USER})
    public void addReservation(ClientNewReservationDto newReservationDto, Long alleyId, String userLogin) throws SsbdApplicationException {
        Timestamp startTime = StringTimestampConverter.getStartDate(newReservationDto).orElseThrow(DataParseException::new);
        Timestamp endTime = StringTimestampConverter.getEndDate(newReservationDto).orElseThrow(DataParseException::new);
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

        List<ReservationItem> items = this.getReservationItemsFromDto(newReservationDto, newReservation);
        checkIfItemsAreAvailable(items, sumUpAllItemQuantitiesFromReservationsWithinGivenTimeFrame(startTime, endTime));

        newReservation.setReservationItems(items);
        try {
            reservationRepositoryLocal.create(newReservation);
        } catch (DataAccessException e) {
            throw e;
        } catch (Exception e) {
            throw new CreateRegistrationException(e.getMessage());
        }
    }

    @Override
    @RolesAllowed(MorRoles.EDIT_OWN_RESERVATION)
    public DetailedReservationDto updateOwnReservation(DetailedReservationDto reservationDto, String userLogin) throws SsbdApplicationException {
        if (!doesReservationBelongToUser(this.ownEditedReservation, userLogin)) {
            throw new NotYourReservationException();
        }
        throwIfReservationExpired();
        throwIfReservationCancelled();

        return updateReservation(reservationDto, this.ownEditedReservation);
    }

    @Override
    @RolesAllowed(MorRoles.EDIT_RESERVATION_FOR_USER)
    public DetailedReservationDto updateReservation(DetailedReservationDto reservationDto) throws SsbdApplicationException {
        throwIfReservationExpired();
        throwIfReservationCancelled();
        return updateReservation(reservationDto, this.userEditedReservation);
    }

    @Override
    @RolesAllowed({MorRoles.CANCEL_OWN_RESERVATION, MorRoles.CANCEL_RESERVATION_FOR_USER})
    public void cancelReservation(Long id) throws SsbdApplicationException {
        if (!reservation.isActive()) {
            throw new ReservationAlreadyInactiveException();
        }
        reservation.setActive(false);
        messenger.sendMessage(
                reservation.getUserAccount().getEmail(),
                localization.get("bowlingAlley") + " - " + localization.get("reservationCanceled"),
                localization.get("yourReservationHasBeenCanceled") + reservation.getId()
        );
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
    @RolesAllowed({MorRoles.EDIT_RESERVATION_FOR_USER})
    public DetailedReservationDto getReservationByIdForEdition(Long id) throws SsbdApplicationException {
        this.userEditedReservation = reservationRepositoryLocal.findById(id)
                .orElseThrow(ReservationDoesNotExistException::new);

        return getDetailedReservationDtoFromEntity(this.userEditedReservation);
    }

    @Override
    @RolesAllowed({MorRoles.GET_OWN_RESERVATION_DETAILS, MorRoles.EDIT_OWN_RESERVATION})
    public DetailedReservationDto getOwnReservationById(Long reservationId, String userLogin) throws SsbdApplicationException {
        Reservation res = reservationRepositoryLocal.findById(reservationId).orElseThrow(ReservationDoesNotExistException::new);
        if (doesReservationBelongToUser(res, userLogin)) {
            this.ownEditedReservation = res;
        } else {
            throw new NotYourReservationException();
        }

        return getDetailedReservationDtoFromEntity(this.ownEditedReservation);
    }


    @Override
    @RolesAllowed({MorRoles.GET_OWN_RESERVATION_DETAILS})
    public ReservationFullDto getUserReservationById(Long id, String login) throws SsbdApplicationException {
        Reservation res = reservationRepositoryLocal.findById(id).orElseThrow(ReservationDoesNotExistException::new);

        if (!res.getUserAccount().getLogin().equals(login)) {
            throw new NotYourReservationException();
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

        if (comment == null) {
            throw new NotFoundException("There is no comment with id: " + id);
        }
        comment.setActive(false);
        reservationRepositoryLocal.edit(reservation);
    }

    private DetailedReservationDto updateReservation(DetailedReservationDto dto, Reservation reservation) throws SsbdApplicationException {
        Timestamp startDate = StringTimestampConverter.getTimestamp(
                dto.getDay(),
                dto.getStartHour()).orElseThrow(DataParseException::new
        );
        Timestamp endDate = StringTimestampConverter.getTimestamp(
                dto.getDay(),
                dto.getEndHour()).orElseThrow(DataParseException::new
        );
        Alley alley = alleyRepositoryLocal.findByNumber(
                dto.getAlleyNumber())
                .orElseThrow(AlleyNotFoundException::new);

        if (!isAlleyAvailable(alley.getNumber(), startDate, endDate, reservation)) {
            throw new AlleyAlreadyReservedException();
        }

        List<ReservationItem> items = this.getReservationItemsFromDto(dto, reservation);
        checkIfItemsAreAvailable(items, sumUpAllItemQuantitiesFromReservationsWithinGivenTimeFrame(startDate, endDate));

        reservation.setAlley(alley);
        reservation.setStartDate(startDate);
        reservation.setEndDate(endDate);
        reservation.setPlayersCount(dto.getNumberOfPlayers());
        updateItems(items, reservation);
        reservationRepositoryLocal.edit(reservation);
        return dto;
    }

    private boolean doesReservationBelongToUser(Reservation reservation, String userLogin) {
        try {
            return reservation.getUserAccount().getLogin().equals(userLogin);
        } catch (NullPointerException e) {
            return false;
        }
    }

    private boolean isAlleyAvailable(int number, Timestamp startDate, Timestamp endDate, Reservation reservation) throws DataAccessException {
        return !reservationRepositoryLocal.getReservationsWithinTimeRange(startDate, endDate)
                .stream()
                .filter(res -> !res.getId().equals(reservation.getId()))
                .map(res -> res.getAlley().getNumber())
                .collect(Collectors.toList())
                .contains(number);
    }

    private List<Integer> getAvailableAlleyNumbersInTimeRangeForReservation(
            Timestamp start,
            Timestamp end,
            Reservation reservation) throws DataAccessException {
        return alleyRepositoryLocal.getAvailableAlleysInTimeRangeExcludingReservation(start, end, reservation.getId())
                .stream()
                .map(Alley::getNumber)
                .collect(Collectors.toList());
    }

    private List<ReservationItem> getReservationItemsFromDto(ClientNewReservationDto dto, Reservation reservation) throws DataAccessException {
        List<ShoesDto> shoesDtos = dto.getShoes();
        List<BallsDto> ballsDtos = dto.getBalls();

        List<ReservationItem> result = new ArrayList<>();

        for (ShoesDto shoe : shoesDtos) {
            result.add(
                    ReservationItem.builder()
                            .reservation(reservation)
                            .count(shoe.getNumber())
                            .item(itemRepositoryLocal.findBySize(shoe.getSize()).orElseThrow(ReservationItemDoesNotExistException::new))
                            .version(0L)
                            .build()
            );
        }

        for (BallsDto ballsDto : ballsDtos) {
            result.add(
                    ReservationItem.builder()
                            .reservation(reservation)
                            .count(ballsDto.getNumber())
                            .item(itemRepositoryLocal.findBySize(ballsDto.getSize()).orElseThrow(ReservationItemDoesNotExistException::new))
                            .version(0L)
                            .build()
            );
        }

        return result;
    }

    private List<ReservationItem> getReservationItemsFromDto(DetailedReservationDto dto, Reservation reservation) throws DataAccessException, ListSizesMismatchException {
        if (dto.getCounts().size() != dto.getSizes().size()) {
            throw new ListSizesMismatchException();
        }

        List<ReservationItem> result = new ArrayList<>();

        for (int i = 0; i < dto.getCounts().size(); ++i) {
            result.add(
                    ReservationItem.builder()
                            .reservation(reservation)
                            .count(dto.getCounts().get(i))
                            .item(itemRepositoryLocal.findBySize(dto.getSizes().get(i)).orElseThrow(ReservationItemDoesNotExistException::new))
                            .version(0L)
                            .build()
            );
        }
        return result;
    }

    private Map<Integer, Integer> sumUpAllItemQuantitiesFromReservationsWithinGivenTimeFrame(Timestamp startDate, Timestamp endDate) throws DataAccessException {
        // get all reservations within time frame
        List<ReservationItem> alreadyReserved =
                reservationItemRepositoryLocal.getReservationItemsFromReservationsWithinTimeFrame(startDate, endDate);

        // sum up all possible items
        return alreadyReserved.stream()
                .collect(Collectors.groupingBy(ri -> ri.getItem().getSize()))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        (Map.Entry<Integer, List<ReservationItem>> entry) -> entry.getValue().stream().mapToInt(ReservationItem::getCount).sum()
                ));
    }

    private void checkIfItemsAreAvailable(List<ReservationItem> newItems, Map<Integer, Integer> reservedItems) throws ReservationItemCountLimitExceededException {
        for (ReservationItem item : newItems) {
            Integer integer = reservedItems.get(item.getItem().getSize());
            int reservedCount = integer == null ? 0 : integer;
            if (item.getCount() + reservedCount > item.getItem().getCount()) {
                throw new ReservationItemCountLimitExceededException();
            }
        }
    }

    private void updateItems(List<ReservationItem> newItems, Reservation reservation) throws DataAccessException {
        List<Long> newIds = newItems.stream()
                .map(ri -> ri.getItem().getId())
                .collect(Collectors.toList());

        List<ReservationItem> toUpdate = reservation.getReservationItems().stream()
                .filter(ri -> newIds.contains(ri.getItem().getId()))
                .collect(Collectors.toList());
        List<Long> idsToUpdate = toUpdate.stream()
                .map(ri -> ri.getItem().getId())
                .collect(Collectors.toList());
        List<ReservationItem> toDelete = reservation.getReservationItems().stream()
                .filter(ri -> !idsToUpdate.contains(ri.getItem().getId()))
                .collect(Collectors.toList());
        List<ReservationItem> toCreate = newItems.stream()
                .filter(ri -> !idsToUpdate.contains(ri.getItem().getId()))
                .collect(Collectors.toList());

        for (ReservationItem item : toDelete) {
            reservationItemRepositoryLocal.delete(item);
            reservation.getReservationItems().remove(item);
        }
        for (ReservationItem item : toCreate) {
            reservation.getReservationItems().add(item);
        }
        for (ReservationItem item : toUpdate) {
            Long id = item.getItem().getId();
            int index = newIds.indexOf(id);
            item.setCount(newItems.get(index).getCount());
        }
    }

    private DetailedReservationDto getDetailedReservationDtoFromEntity(Reservation entity) throws SsbdApplicationException {

        Pair<Optional<String>, Optional<String>> startDateTime = StringTimestampConverter.getDateAndTimeStrings(entity.getStartDate());
        Pair<Optional<String>, Optional<String>> endDateTime = StringTimestampConverter.getDateAndTimeStrings(entity.getEndDate());

        return DetailedReservationDto.builder()
                .day(startDateTime.getValue0().orElse(""))
                .startHour(startDateTime.getValue1().orElse(""))
                .endHour(endDateTime.getValue1().orElse(""))
                .numberOfPlayers(entity.getPlayersCount())
                .alleyNumber(entity.getAlley().getNumber())
                .availableAlleyNumbers(this.getAvailableAlleyNumbersInTimeRangeForReservation(
                        entity.getStartDate(),
                        entity.getEndDate(),
                        entity
                ))
                .items(entity.getReservationItems()
                        .stream()
                        .map(ri -> new ReservationItemDto(ri.getItem().getSize(), ri.getCount()))
                        .collect(Collectors.toList())
                )
                .build();
    }

    private void throwIfReservationCancelled() throws UpdateInactiveReservationException {
        if (!this.ownEditedReservation.isActive()) {
            throw new UpdateInactiveReservationException();
        }
    }

    private void throwIfReservationExpired() throws UpdateExpiredReservationExcetpion {
        if (ReservationValidator.isExpired(this.ownEditedReservation.getStartDate())) {
            throw new UpdateExpiredReservationExcetpion();
        }
    }
}
