package pl.lodz.p.it.ssbd2019.ssbd03.mot.service;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.web.dto.ItemDto;

import java.util.List;

/**
 * Interfejs udostępniający metody związane z operacjami na encji Item
 */
public interface ItemService {
    /**
     * Aktualizuje ilość wybranych przedmiotów
     * @throws SsbdApplicationException gdy aktualizacja się nie powiedzie
     * @param items zaktualizowane przedmioty
     */
    void updateItems(List<ItemDto> items) throws SsbdApplicationException;

    /**
     * Pobiera przedmioty o podanym rodzaju przedmiotu
     *
     * @param itemType rodzaj przedmiotu
     * @return Lista obiektów encji przechowująca obiekty typu Item
     */
    List<ItemDto> getItemsBySpecifiedItemType(String itemType);
}
