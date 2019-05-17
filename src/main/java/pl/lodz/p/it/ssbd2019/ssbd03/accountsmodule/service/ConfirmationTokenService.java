package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.ConfirmationTokenException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.TokenNotFoundException;

/**
 * Klasa reprezentująca logikę biznesową dla operacji związanych z obiektami oraz encjami klasy ConfirmationToken.
 */
public interface ConfirmationTokenService {
    void activateAccountByToken(String string) throws TokenNotFoundException, ConfirmationTokenException;
    void createNewTokenForAccount(UserAccount userAccount) throws ConfirmationTokenException;
}
