package pl.lodz.p.it.ssbd2019.ssbd03.mor.service;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.AvailableAlleyDto;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.DetailedReservationDto;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.NewReservationDto;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.ReservationFullDto;

import java.sql.Timestamp;
import java.util.List;

public interface ReservationService {

    /**
     * Zwraca tory, które nie są zarezerwowane dla zadanego przedziału czasu.
     *
     * @param newReservationDto przedział czasu
     * @return lista torów
     * @throws SsbdApplicationException w razie błędu
     */
    List<AvailableAlleyDto> getAvailableAlleysInTimeRange(NewReservationDto newReservationDto) throws SsbdApplicationException;
    
    
    /**
     * Zwraca tory, które nie są zarezerwowane dla zadanego przedziału czasu.
     *
     * @param start początek okresu
     * @param end koniec okresu
     * @return lista torów
     * @throws SsbdApplicationException w razie błędu
     */
    List<AvailableAlleyDto> getAvailableAlleysInTimeRange(Timestamp start, Timestamp end) throws SsbdApplicationException;
    
    /**
     * Zwraca tory, które nie są zarezerwowane w podanym okresie nie uwzględniając własnej obecnie edytowanej rezerwacji
     * @param start początek okresu
     * @param end koniec okresu
     * @return lista torów
     * @throws SsbdApplicationException w razie błędu
     */
    List<AvailableAlleyDto> getAvailableAlleysInTimeRangeExcludingOwnReservation(Timestamp start, Timestamp end) throws SsbdApplicationException;
    
    /**
     * Dokonuje rezerwacji.
     *
     * @param newReservationDto dane rezerwacji
     * @param alleyId           numer toru
     * @param userLogin         login użytkownika
     * @throws SsbdApplicationException w razię błędu
     */
    void addReservation(NewReservationDto newReservationDto, Long alleyId, String userLogin) throws SsbdApplicationException;

    /**
     * Wprowadza dane dotyczące zakończonej rozgrywki.
     *
     * @param reservationDto Obiekt rezerwacji przechowujący zaktualizowane dane.
     * @param userLogin login użytkownika
     * @return Dto edytowanej encji
     * @throws SsbdApplicationException w razie błędu
     */
    DetailedReservationDto updateReservation(DetailedReservationDto reservationDto, String userLogin)  throws SsbdApplicationException;

    /**
     * Odwołuje wybraną rezerwację
     *
     * @param id identyfikator rezerwacji do odwołoania
     */
    void cancelReservation(Long id) throws SsbdApplicationException;

    /**
     * Pobiera rezerwacje wybranego użytkownika
     *
     * @param userId id użytkownika
     * @return Lista rezerwacji użytkownika
     */
    List<ReservationFullDto> getReservationsForUser(Long userId) throws SsbdApplicationException;

    /**
     * Pobiera rezerwacje wybranego użytkownika
     *
     * @param login login użytkownika
     * @return Lista rezerwacji użytkownika
     */
    List<ReservationFullDto> getReservationsByUserLogin(String login) throws SsbdApplicationException;

    /**
     * Pobiera wszystkie rezerwacje dla podanego toru
     *
     * @param alleyId identyfikator toru
     * @return Lista rezerwacji dla danego toru
     */
    List<ReservationFullDto> getReservationsForAlley(Long alleyId) throws SsbdApplicationException;

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
     * Pobiera wybraną rezerwację dla użytkownika
     *
     * @param reservationId identyfikator rezerwacji
     * @param userLogin login użytkownika
     * @return encja rezerwacji
     * @throws SsbdApplicationException nie udało się uzyskać dostępu, rezerwacja nie istnieje lub nie należy do podanego użytkownika
     */
    DetailedReservationDto getOwnReservationById(Long reservationId, String userLogin) throws SsbdApplicationException;

    /**
     * Blokuje wybrany komentarz
     * @param id identyfikator komentarza
     * @throws SsbdApplicationException gdy zablokowanie komentarza się nie powiedzie
     */
    void disableComment(Long id) throws SsbdApplicationException;
}
