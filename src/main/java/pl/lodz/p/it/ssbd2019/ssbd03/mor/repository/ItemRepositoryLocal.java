package pl.lodz.p.it.ssbd2019.ssbd03.mor.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Item;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.EntityRetrievalException;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.CruRepository;

import javax.ejb.Local;
import java.util.Optional;

@Local
public interface ItemRepositoryLocal extends CruRepository<Item, Long> {
    
    /**
     * Wyszukuje w bazie rekordu przedmiotu o podanym rozmiarze
     *
     * @param size rozmiar przedmiotu
     * @return encja przedmiotu
     * @throws EntityRetrievalException w razie błędu z połączeniem z bazą
     */
    Optional<Item> findBySize(int size) throws EntityRetrievalException;
}
