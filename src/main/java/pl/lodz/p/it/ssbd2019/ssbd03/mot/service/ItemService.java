package pl.lodz.p.it.ssbd2019.ssbd03.mot.service;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Item;

import java.util.List;

public interface ItemService {
    /**
     * Aktualizuje ilość konkretnego przedmiotu
     *
     * @param id identyfikator przedmiotu
     * @param count ilość przedmiotu na stanie
     */
    void updateItemCount(Long id, int count);

    /**
     * Pobiera przedmioty o podanym rodzaju przedmiotu
     *
     * @param itemType rodzaj przedmiotu
     * @return Lista obiektów encji przechowująca obiekty typu Item
     */
    List<Item> getItemsBySpecifiedItemType(String itemType);
}
