package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.ChangePasswordException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityRetrievalException;

/**
 * Klasa reprezentująca logikę biznesową dla operacji związanych z obiektami oraz encjami klasy Account.
 */
public interface AccountService {
    /**
     * Zmienia hasło użytkownika o podanym loginie
     *
     * @param login           login użytkownika
     * @param currentPassword aktualne hasło użytkownika
     * @param newPassword     nowe hasło użytkownika
     * @return konto z zaktualizowanym hasłem
     * @throws ChangePasswordException wyjątek zmiany hasła
     */
    Account changePassword(String login, String currentPassword, String newPassword) throws ChangePasswordException;

    /**
     * Zwraca konto użytkownika o podanym loginie
     *
     * @param login login użytkownika
     * @return konto użytkownika o podanym loginie
     */
    Account findByLogin(String login) throws EntityRetrievalException;
}
