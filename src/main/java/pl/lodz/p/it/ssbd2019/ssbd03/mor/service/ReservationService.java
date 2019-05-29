package pl.lodz.p.it.ssbd2019.ssbd03.mor.service;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Comment;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.Reservation;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.List;

@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public interface ReservationService {
    /**
     * Tworzy rezerwację
     *
     * @param reservation Obiekt rezerwacji.
     * @param userLogin Login użytkownika dla którego ma być dokonana rezerwacja.
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
     * @param userLogin login użytkownika
     * @return Lista rezerwacji użytkownika
     */
    List<Reservation> getReservationsForUser(String userLogin);

    /**
     * Pobiera wszystkie rezerwacje dla podanego toru
     *
     * @param alleyId identyfikator toru
     * @return Lista rezerwacji dla danego toru
     */
    List<Reservation> getReservationsForAlley(Long alleyId);

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
     * @param id reservationId
     * @param comment Obiekt komentarza
     */
    void addCommentForReservation(Long id, Comment comment);
}
