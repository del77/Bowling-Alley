package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccessLevel;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccountAccessLevel;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccountAccessLevelId;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.CruRepository;

import javax.ejb.Local;
import java.util.List;

@Local
public interface AccountAccessLevelRepositoryLocal extends CruRepository<AccountAccessLevel, AccountAccessLevelId> {
    /**
     * Zwraca powiązania pomiędzy kontem użytkownika a poziomami dostępu.
     * Metoda dla pozyskiwania poziomów dostępu dla konta.
     * @param account Obiekt encji konta użytkownika
     * @return Lista powiązań między kontami użytkowników, a poziomami dostępu.
     */
    List<AccountAccessLevel> findAllForAccount(UserAccount account);

    /**
     * Zwraca powiązania pomiędzy kontem użytkownika a poziomami dostępu.
     * Metoda dla pozyskiwania konta użytkowników dla poziomu dostępu.
     * @param accessLevel Obiekt encji poziomu dostępu
     * @return Lista powiązań między kontami użytkowników, a poziomami dostępu.
     */
    List<AccountAccessLevel> findAllForAccessLevel(AccessLevel accessLevel);
}
