package pl.lodz.p.it.ssbd2019.ssbd03.repository;

import java.util.List;
import java.util.Optional;

/**
 * Generyczny interfejs repozytorium (fasada).
 * @param <T> Typ encji
 * @param <ID> Typ identyfikatora
 */
public interface CruRepository<T, ID> {

    /**
     * Dodawanie obiektu do magazynu danych.
     * @param entity Obiekt encji
     */
    T create(T entity);

    /**
     * Edycja istniejącej encji.
     * @param entity Obiekt encji
     */
    T edit(T entity);

    /**
     * Zwraca z magazynu danych encję o zadanym identyfikatorze.
     * @param id Identyfikator encji
     * @return Opakowany w klasę Optional obiekt encji
     */
    Optional<T> findById(ID id);

    /**
     * Sprawdza czy obiekt istnieje w magazynie danych.
     * @param id Identyfikator encji
     * @return true w przypadku, gdy obiekt istnieje w magazynie danych, w przeciwnym wypadku false
     */
    boolean existsById(ID id);

    /**
     * Zwraca listę wszystkich encji w magazynie danych.
     * @return Lista wszystkich encji
     */
    List<T> findAll();
}