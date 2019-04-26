package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.ChangePasswordException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityCreationException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityRetrievalException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityUpdateException;

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
     * Metoda dodaje uzytkownika do bazy danych oraz zwraca dodaną encję.
     * @param userAccount Obiekt typu UserAccount, którego dane mają być dodane do bazy danych.
     * @return Encja użytkowniak dodanego do bazy danych.
     * @throws EntityCreationException w wypadku gdy nie powiedzie się tworzenie encji.
     */
    UserAccount addUser(UserAccount userAccount) throws EntityCreationException;

    /**
     * Aktualizuje użytkownika w bazie danych. Użytkownik musi być zawarty w obecnym kotekście (sesji).
     * @param userAccount Encja użytkownika do zaktualizowania.
     * @return Zaktualizowana encja uzytkownika.
     * @throws EntityUpdateException w wypadku, gdy nie uda się aktualizacja.
     */
    UserAccount updateUser(UserAccount userAccount) throws EntityUpdateException;


    UserAccount getByLogin(String login) throws EntityRetrievalException;

    /**
     * Zmienia hasło użytkownika o podanym loginie
     *
     * @param login           login użytkownika
     * @param currentPassword aktualne hasło użytkownika
     * @param newPassword     nowe hasło użytkownika
     * @return konto z zaktualizowanym hasłem
     * @throws ChangePasswordException wyjątek zmiany hasła
     */
    UserAccount changePassword(String login, String currentPassword, String newPassword) throws ChangePasswordException;
}
