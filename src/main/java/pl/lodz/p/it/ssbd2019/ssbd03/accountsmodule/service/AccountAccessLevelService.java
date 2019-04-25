package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccountAccessLevel;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityRetrievalException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityUpdateException;

import java.util.List;

public interface AccountAccessLevelService {
    List<AccountAccessLevel> getAccountAccessLevelsByUserId(Long id) throws EntityRetrievalException;
    void updateAccountAccessLevels(AccountAccessLevel accountAccessLevel) throws EntityUpdateException;
}
