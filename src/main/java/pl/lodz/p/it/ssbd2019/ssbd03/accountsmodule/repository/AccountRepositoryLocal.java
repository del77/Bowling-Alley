package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.CruRepository;

import javax.ejb.Local;
import java.util.Optional;

@Local
public interface AccountRepositoryLocal extends CruRepository<Account, Long> {
    Optional<Account> findByLogin(String login);
}
