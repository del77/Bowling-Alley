package pl.lodz.p.it.ssbd2019.ssbd03.mor.service;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.ReservationItemsDto;

public interface ReservationItemService {
    /**
     * Zwraca przedmioty zarezerwowane w danej rezerwacji
     *
     * @param reservationId identyfikator rezerwacji
     * @return dto zawierające listy parametrów przedmiotów
     * @throws SsbdApplicationException w razie błędu pobierania danych z bazy
     */
    ReservationItemsDto getItemsForReservation(long reservationId) throws SsbdApplicationException;
}
