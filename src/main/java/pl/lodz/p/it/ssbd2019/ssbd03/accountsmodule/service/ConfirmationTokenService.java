package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.generalized.ConfirmationTokenException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.notfound.TokenNotFoundException;

/**
 * Klasa reprezentująca logikę biznesową dla operacji związanych z obiektami oraz encjami klasy ConfirmationToken.
 */
public interface ConfirmationTokenService {
    /**
     * Metoda służy do potwierdzenia konta na bazie podanego tokena.
     * Metoda może wyrzucić błąd również w przypadku, gdy użytkownik nie isntieje.
     * @param token wartość tokena potwierdzenia
     * @throws TokenNotFoundException W przypadku, gyd taki token nie istnieje w bazie danych.
     * @throws ConfirmationTokenException Gdy wystapi błąd w trakcie przetwarzania. M.in. gdy użytkownik nie istnieje lub
     * jest już potwierdzone jego konto.
     */
    void activateAccountByToken(String token) throws SsbdApplicationException;

    /**
     * Metoda tworzy token dla użytkowniak o podanej nazwie.
     * @param userAccount konto użytkownika.
     * @throws ConfirmationTokenException W przypadku, gdy nie udaje się potwierdzić, tj. np. użytkownik o danym loginie
     * nie istnieje bądź wystapi błąd bazy danych.
     */
    void createNewTokenForAccount(UserAccount userAccount) throws SsbdApplicationException;
}
