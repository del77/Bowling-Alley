package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.User;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.CruRepository;

import javax.ejb.Local;
import java.util.Optional;

@Local
public interface UserRepositoryLocal extends CruRepository<User, Long> {
    Optional<User> findByLogin(String login);
}
