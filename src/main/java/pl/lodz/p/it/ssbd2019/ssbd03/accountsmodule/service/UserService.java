package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.User;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityCreationException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityRetrievalException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityUpdateException;

import java.util.List;

/**
 * Klasa reprezentująca logikę biznesową dla operacji związanych z obiektami oraz encjami klasy User.
 */
public interface UserService {

    /**
     * Metoda zwracajaca listę wszystkich uzytkowników w bazie danych.
     * @return Lista encji użytkownika.
     * @throws EntityRetrievalException w wypadku gdy nie powiedzie się pobieranie użytkownika z bazy danych.
     */
    List<User> getAllUsers() throws EntityRetrievalException;

    /**
     * Metoda pobiera z bazy danych uzytkownika o podanym id.
     * @param id Identyfikator uzytkownika, którego należy pobrać z bazy danych.
     * @return Użytkownik o zadanym id.
     * @throws EntityRetrievalException w wypadku gdy nie powiedzie się pobieranie użytkownika z bazy danych,
     * bądź gdy nie znajdzie użytkownika.
     */
    User getUserById(Long id) throws EntityRetrievalException;

    /**
     * Metoda dodaje uzytkownika do bazy danych oraz zwraca dodaną encję.
     * @param user Obiekt typu User, którego dane mają być dodane do bazy danych.
     * @return Encja użytkowniak dodanego do bazy danych.
     * @throws EntityCreationException w wypadku gdy nie powiedzie się tworzenie encji.
     */
    User addUser(User user) throws EntityCreationException;

    /**
     * Aktualizuje użytkownika w bazie danych. Użytkownik musi być zawarty w obecnym kotekście (sesji).
     * @param user Encja użytkownika do zaktualizowania.
     * @return Zaktualizowana encja uzytkownika.
     * @throws EntityUpdateException w wypadku, gdy nie uda się aktualizacja.
     */
    User updateUser(User user) throws EntityUpdateException;

}
