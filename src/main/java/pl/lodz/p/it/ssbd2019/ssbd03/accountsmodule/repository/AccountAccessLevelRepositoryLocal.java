package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccessLevel;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccountAccessLevel;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccountAccessLevelId;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.CruRepository;

import javax.ejb.Local;
import java.util.List;

@Local
public interface AccountAccessLevelRepositoryLocal extends CruRepository<AccountAccessLevel, AccountAccessLevelId> {
    List<AccountAccessLevel> findAllForAccount(Account account);
    List<AccountAccessLevel> findAllForAccessLevel(AccessLevel accessLevel);
}
