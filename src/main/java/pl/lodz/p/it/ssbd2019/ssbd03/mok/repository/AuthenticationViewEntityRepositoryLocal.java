package pl.lodz.p.it.ssbd2019.ssbd03.mok.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.AuthenticationViewEntity;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.AuthenticationViewEntityId;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.CruRepository;

import javax.ejb.Local;
import java.util.List;

@Local
public interface AuthenticationViewEntityRepositoryLocal extends CruRepository<AuthenticationViewEntity, AuthenticationViewEntityId> {
    
    /**
     * Metoda zwraca wszystkie rekordy z widoku dla podanego loginu
     *
     * @param login login konta
     * @return Lista rekordów, w którym dany login występuje
     */
    List<AuthenticationViewEntity> findAllWithLogin(String login);
}
