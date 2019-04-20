package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.User;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityRetrievelException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.RegistrationProcessException;

public interface RegistrationService {
    void registerAccount(Account account, User user) throws RegistrationProcessException, EntityRetrievelException;

    /**
     * Potwierdzenie konta użytkownika.
     * Metoda powstała na potrzeby weryfikacji po aktywacji po e-mail.
     * @param accountId Identyfikator konta
     */
    void confirmAccount(long accountId) throws EntityRetrievelException;
}
