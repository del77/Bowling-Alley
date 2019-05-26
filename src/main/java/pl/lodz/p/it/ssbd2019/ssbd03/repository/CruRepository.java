package pl.lodz.p.it.ssbd2019.ssbd03.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.DataAccessException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.EntityUpdateException;

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
    T create(T entity) throws DataAccessException;

    /**
     * Edycja istniejącej encji.
     * @param entity Obiekt encji
     * @return encja po edycji
     * @throws EntityUpdateException rzucony, gdy edycja się nie powiedzie
     */
    T edit(T entity) throws DataAccessException;
    
    /**
     * Edycja istniejącej encji bez wykonywania operacji merge.
     * @param entity Obiekt encji
     * @return encja po edycji
     * @throws EntityUpdateException rzucony, gdy edycja się nie powiedzie
     */
    T editWithoutMerge(T entity) throws DataAccessException;

    /**
     * Zwraca z magazynu danych encję o zadanym identyfikatorze.
     * @param id Identyfikator encji
     * @return Opakowany w klasę Optional obiekt encji
     */
    Optional<T> findById(ID id) throws DataAccessException;

    /**
     * Sprawdza czy obiekt istnieje w magazynie danych.
     * @param id Identyfikator encji
     * @return true w przypadku, gdy obiekt istnieje w magazynie danych, w przeciwnym wypadku false
     */
    boolean existsById(ID id) throws DataAccessException;

    /**
     * Zwraca listę wszystkich encji w magazynie danych.
     * @return Lista wszystkich encji
     */
    List<T> findAll() throws DataAccessException;

    /**
     * Odświeża stan istniejącej encji.
     * @param entity obiekt encji, który należy odświeżyć.
     * @return Obiekt encji.
     */
    T refresh(T entity);
}