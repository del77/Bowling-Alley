package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccessLevel;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityRetrievalException;

public interface AccessLevelService {
    AccessLevel getAccessLevelByName(String name) throws EntityRetrievalException;
}
