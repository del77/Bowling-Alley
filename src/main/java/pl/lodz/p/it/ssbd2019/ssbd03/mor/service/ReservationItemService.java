package pl.lodz.p.it.ssbd2019.ssbd03.mor.service;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.ReservationItemDto;

import java.util.List;

public interface ReservationItemService {
    /**
     * Zwraca przedmioty zarezerwowane w danej rezerwacji
     *
     * @param reservationId identyfikator rezerwacji
     * @return lista dto zawierających parametry przedmiotów
     * @throws SsbdApplicationException w razie błędu pobierania danych z bazy
     */
    List<ReservationItemDto> getItemsForReservation(long reservationId) throws SsbdApplicationException;
}
