package pl.lodz.p.it.ssbd2019.ssbd03.mot.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Item;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.CruRepository;

import javax.ejb.Local;
import java.util.List;

@Local
public interface ItemRepositoryLocal extends CruRepository<Item, Long> {
    /**
     * Metoda służy do pozyskiwania encji przedmiotów na podstawie podanego typu przedmiotu
     *
     * @param itemType typ przedmiotu
     * @return przedmioty o podanym typie
     */
    List<Item> findAllByItemType(String itemType);
}
