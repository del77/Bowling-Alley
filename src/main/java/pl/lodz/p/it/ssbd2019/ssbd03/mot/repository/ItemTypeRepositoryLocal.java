package pl.lodz.p.it.ssbd2019.ssbd03.mot.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.ItemType;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.CruRepository;

import java.util.Optional;

public interface ItemTypeRepositoryLocal extends CruRepository<ItemType, Long> {
    /**
     * Pobiera rodzaj przedmiotu o podanej nazwie
     *
     * @param type Rodzaj przedmiotu
     * @return Encja reprezentująca konto rodzaj przedmiotu.
     */
    Optional<ItemType> findByType(String type);
}
