package pl.lodz.p.it.ssbd2019.ssbd03.mor.service;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Comment;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.Reservation;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.DataAccessException;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.ReservationDto;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.List;

public interface ReservationService {
    /**
     * Tworzy rezerwację
     *
     * @param reservation Obiekt rezerwacji.
     * @param userLogin   Login użytkownika dla którego ma być dokonana rezerwacja.
     */
    void addReservation(Reservation reservation, String userLogin);

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
     */
    List<Reservation> getReservationsById(Long id);

    /**
     * Dodaje komentarz do zakończonej rezerwacji
     *
     * @param id      reservationId
     * @param comment Obiekt komentarza
     */
    void addCommentForReservation(Long id, Comment comment);
}
