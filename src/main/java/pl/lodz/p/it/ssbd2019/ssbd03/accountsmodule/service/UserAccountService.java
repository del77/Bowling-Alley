package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.conflict.validation.NotUniqueEmailException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.EntityRetrievalException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.EntityUpdateException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.generalized.ChangePasswordException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.notfound.LoginDoesNotExistException;

import java.util.List;

/**
 * Klasa reprezentująca logikę biznesową dla operacji związanych z obiektami oraz encjami klasy UserAccount.
 */
public interface UserAccountService {

    /**
     * Metoda zwracajaca listę wszystkich uzytkowników w bazie danych.
     * @return Lista encji użytkownika.
     * @throws EntityRetrievalException w wypadku gdy nie powiedzie się pobieranie użytkownika z bazy danych.
     */
    List<UserAccount> getAllUsers() throws EntityRetrievalException;

    /**
     * Metoda pobiera z bazy danych uzytkownika o podanym id.
     * @param id Identyfikator uzytkownika, którego należy pobrać z bazy danych.
     * @return Użytkownik o zadanym id.
     * @throws EntityRetrievalException w wypadku gdy nie powiedzie się pobieranie użytkownika z bazy danych,
     * bądź gdy nie znajdzie użytkownika.
     */
    UserAccount getUserById(Long id) throws EntityRetrievalException;

    /**
     * Aktualizuje dane użytkownika w bazie danych. Użytkownik musi być zawarty w obecnym kotekście (sesji).
     * @param userAccount Encja użytkownika do zaktualizowania.
     * @return Zaktualizowana encja uzytkownika.
     * @throws EntityUpdateException w wypadku, gdy nie uda się aktualizacja.
     * @throws NotUniqueEmailException w wypadku, gdy nowy email nie jest unikalny
     */
    UserAccount updateUser(UserAccount userAccount) throws EntityUpdateException, NotUniqueEmailException;
    
    /**
     * Aktualizuje poziomy dostępu użytkownika w bazie danych. Użytkownik musi być zawarty w obecnym kotekście (sesji).
     * @param userAccount Encja użytkownika do zaktualizowania.
     * @param selectedAccessLevels Przydzielone użytkownikowi poziomy dostępu.
     * @return Zaktualizowana encja uzytkownika.
     * @throws EntityUpdateException w wypadku, gdy nie uda się aktualizacja.
     */
    UserAccount updateUserAccessLevels(UserAccount userAccount, List<String> selectedAccessLevels) throws EntityUpdateException;

    /**
     * Metoda pobiera z bazy danych uzytkownika o podanym loginie.
     * @param login Login uzytkownika, którego należy pobrać z bazy danych.
     * @return Użytkownik o zadanym loginie.
     * @throws EntityRetrievalException w wypadku gdy nie powiedzie się pobieranie użytkownika z bazy danych,
     * bądź gdy nie znajdzie użytkownika.
     */
    UserAccount getByLogin(String login) throws EntityRetrievalException, LoginDoesNotExistException;

    /**
     * Metoda pozwalająca zmienić hasło użytkownika o podanym loginie.
     * Wymagane jest podanie obecnego hasła.
     *
     * @param login           login użytkownika
     * @param currentPassword aktualne hasło użytkownika
     * @param newPassword     nowe hasło użytkownika
     * @throws ChangePasswordException wyjątek zmiany hasła
     */
    void changePasswordByLogin(String login, String currentPassword, String newPassword) throws ChangePasswordException;

    /**
     * Zmienia flagę zablokowania konta użytkownika z podanym id
     *
     * @param id identyfikator użytkownika
     * @param isActive nowa wartość flagi zablokowania
     * @return obiekt encji odblokowanego użytkownika
     * @throws EntityUpdateException w wypadku, gdy nie uda się aktualizacja.
     */
    UserAccount updateLockStatusOnAccountById(Long id, boolean isActive) throws EntityUpdateException;

    /**
     * Metoda pozwalająca zmienić hasło użytkownika o podanym id.
     *
     * @param id           identyfikator użytkownika
     * @param newPassword     nowe hasło użytkownika
     * @throws ChangePasswordException wyjątek zmiany hasła
     */
    void changePasswordById(long id, String newPassword) throws ChangePasswordException;
    
    /**
     * Resetuje licznik nieudanych prób logowania dla konta o podanym loginie
     *
     * @param login login konta
     * @throws EntityUpdateException w wypadku, gdy nie uda się zmiana stanu encji
     */
    void restartFailedLoginsCounter(String login) throws EntityUpdateException;
    
    /**
     * Zwiększa licznik nieudanych prób logowania dla konta o podanym loginie
     * i blokuje konto, jeśli licznik osiągnął wartość 3
     *
     * @param login login konta
     * @throws EntityUpdateException w wypadku, gdy nie uda się zmiana stanu encji
     */
    void incrementFailedLoginsCounter(String login) throws EntityUpdateException;
}
