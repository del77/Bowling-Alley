package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.ConfirmationTokenException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.TokenNotFoundException;

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
    void activateAccountByToken(String token) throws TokenNotFoundException, ConfirmationTokenException;

    /**
     * Metoda tworzy token dla użytkowniak o podanej nazwie.
     * @param userName nazwa użytkownika.
     * @throws ConfirmationTokenException W przypadku, gdy nie udaje się potwierdzić, tj. np. użytkownik o danym loginie
     * nie istnieje bądź wystapi błąd bazy danych.
     */
    void createNewTokenForAccount(String userName) throws ConfirmationTokenException;
}
