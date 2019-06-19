package pl.lodz.p.it.ssbd2019.ssbd03.mor.service;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Comment;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.Reservation;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.AvailableAlleyDto;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.NewReservationDto;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.ReservationDto;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.ReservationFullDto;

import java.util.List;

public interface ReservationService {

    /**
     * Zwraca tory, które nie są zarezerwowane dla zadanego przedziału czasu.
     *
     * @param newReservationDto przedział czasu
     * @return lista torów
     * @throws SsbdApplicationException
     */
    List<AvailableAlleyDto> getAvailableAlleysInTimeRange(NewReservationDto newReservationDto) throws SsbdApplicationException;

    /**
     * Dokonuje rezerwacji.
     *
     * @param newReservationDto dane rezerwacji
     * @param alleyId           numer toru
     * @param userLogin         login użytkownika
     * @throws SsbdApplicationException
     */
    void addReservation(NewReservationDto newReservationDto, Long alleyId, String userLogin) throws SsbdApplicationException;

    /**
     * Wprowadza dane dotyczące zakończonej rozgrywki.
     *
     * @param reservation Obiekt rezerwacji przechowujący zaktualizowane dane.
     */
    void updateReservation(Reservation reservation);

    /**
     * Odwołuje wybraną rezerwację
     *
     * @param id identyfikator rezerwacji do odwołoania
     */
    void cancelReservation(Long id);

    /**
     * Pobiera rezerwacje wybranego użytkownika
     *
     * @param userId id użytkownika
     * @return Lista rezerwacji użytkownika
     */
    List<ReservationDto> getReservationsForUser(Long userId) throws SsbdApplicationException;

    /**
     * Pobiera wszystkie rezerwacje dla podanego toru
     *
     * @param alleyId identyfikator toru
     * @return Lista rezerwacji dla danego toru
     */
    List<ReservationDto> getReservationsForAlley(Long alleyId) throws SsbdApplicationException;

    /**
     * Pobiera wybraną rezerwację
     *
     * @param id identyfikator rezerwacji
     * @return obiekt wybranej rezerwacji
     * @throws SsbdApplicationException rezerwacja nie istnieje lub nie udało się uzyskać dostępu do danych
     */
    ReservationFullDto getReservationById(Long id) throws SsbdApplicationException;

    /**
     * Pobiera wybraną rezerwację dla użytkownika
     *
     * @param id    identyfikator rezerwacji
     * @param login login użytkownika
     * @return obiekt wybranej rezerwacji
     * @throws SsbdApplicationException rezerwacja nie istnieje lub nie udało się uzyskać dostępu do danych
     */
    ReservationFullDto getUserReservationById(Long id, String login) throws SsbdApplicationException;

    /**
     * Dodaje komentarz do zakończonej rezerwacji
     *
     * @param id      reservationId
     * @param comment Obiekt komentarza
     */
    void addCommentForReservation(Long id, Comment comment);
}
