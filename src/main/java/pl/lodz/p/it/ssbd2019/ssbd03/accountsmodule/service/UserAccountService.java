package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.EntityRetrievalException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.EntityUpdateException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.NotUniqueEmailException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.generalized.ChangePasswordException;

import java.util.List;

/**
 * Klasa reprezentująca logikę biznesową dla operacji związanych z obiektami oraz encjami klasy UserAccount.
 */
public interface UserAccountService {

    /**
     * Metoda zwracajaca listę wszystkich uzytkowników w bazie danych.
     * @return Lista encji użytkownika.
     * @throws SsbdApplicationException w wypadku gdy nie powiedzie się pobieranie użytkownika z bazy danych.
     */
    List<UserAccount> getAllUsers() throws SsbdApplicationException;

    /**
     * Metoda pobiera z bazy danych uzytkownika o podanym id.
     * @param id Identyfikator uzytkownika, którego należy pobrać z bazy danych.
     * @return Użytkownik o zadanym id.
     * @throws SsbdApplicationException w wypadku gdy nie powiedzie się pobieranie użytkownika z bazy danych,
     * bądź gdy nie znajdzie użytkownika.
     */
    UserAccount getUserById(Long id) throws SsbdApplicationException;

    /**
     * Aktualizuje dane użytkownika w bazie danych. Użytkownik musi być zawarty w obecnym kotekście (sesji).
     * @param userAccount Encja użytkownika do zaktualizowania.
     * @return Zaktualizowana encja uzytkownika.
     * @throws SsbdApplicationException w wypadku, gdy nie uda się aktualizacja.
     */
    UserAccount updateUser(UserAccount userAccount) throws SsbdApplicationException;
    
    /**
     * Aktualizuje poziomy dostępu użytkownika w bazie danych. Użytkownik musi być zawarty w obecnym kotekście (sesji).
     * @param userAccount Encja użytkownika do zaktualizowania.
     * @param selectedAccessLevels Przydzielone użytkownikowi poziomy dostępu.
     * @return Zaktualizowana encja uzytkownika.
     * @throws SsbdApplicationException w wypadku, gdy nie uda się aktualizacja.
     */
    UserAccount updateUserAccessLevels(UserAccount userAccount, List<String> selectedAccessLevels) throws SsbdApplicationException;

    /**
     * Metoda pobiera z bazy danych uzytkownika o podanym loginie.
     * @param login Login uzytkownika, którego należy pobrać z bazy danych.
     * @return Użytkownik o zadanym loginie.
     * @throws SsbdApplicationException w wypadku gdy nie powiedzie się pobieranie użytkownika z bazy danych,
     * bądź gdy nie znajdzie użytkownika.
     */
    UserAccount getByLogin(String login) throws SsbdApplicationException;

    /**
     * Metoda pozwalająca zmienić hasło użytkownika o podanym loginie.
     * Wymagane jest podanie obecnego hasła.
     *
     * @param login           login użytkownika
     * @param currentPassword aktualne hasło użytkownika
     * @param newPassword     nowe hasło użytkownika
     * @throws SsbdApplicationException wyjątek zmiany hasła
     */
    void changePasswordByLogin(String login, String currentPassword, String newPassword) throws SsbdApplicationException;

    /**
     * Zmienia flagę zablokowania konta użytkownika z podanym id
     *
     * @param id identyfikator użytkownika
     * @param isActive nowa wartość flagi zablokowania
     * @return obiekt encji odblokowanego użytkownika
     * @throws SsbdApplicationException w wypadku, gdy nie uda się aktualizacja.
     */
    UserAccount updateLockStatusOnAccountById(Long id, boolean isActive) throws SsbdApplicationException;

    /**
     * Metoda pozwalająca zmienić hasło użytkownika o podanym id.
     *
     * @param id           identyfikator użytkownika
     * @param newPassword     nowe hasło użytkownika
     * @throws SsbdApplicationException gdy nie uda się zmienic hasła
     */
    void changePasswordById(long id, String newPassword) throws SsbdApplicationException;
    
    /**
     * Resetuje licznik nieudanych prób logowania dla konta o podanym loginie
     *
     * @param login login konta
     * @throws SsbdApplicationException w wypadku, gdy nie uda się zmiana stanu encji
     */
    void restartFailedLoginsCounter(String login) throws SsbdApplicationException;
    
    /**
     * Zwiększa licznik nieudanych prób logowania dla konta o podanym loginie
     * i blokuje konto, jeśli licznik osiągnął wartość 3
     *
     * @param login login konta
     * @throws SsbdApplicationException w wypadku, gdy nie uda się zmiana stanu encji
     */
    void incrementFailedLoginsCounter(String login) throws SsbdApplicationException;
}
