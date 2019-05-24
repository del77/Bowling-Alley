package pl.lodz.p.it.ssbd2019.ssbd03.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityUpdateException;

import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Optional;

/**
 * Abstrakcyjna implementacja CruRepository, dostarczająca podstawowe metody dla bazy danych.
 * @param <T> Typ encji
 * @param <ID> Typ klucza głównego
 */
public abstract class AbstractCruRepository<T, ID> implements CruRepository<T, ID> {

    protected abstract EntityManager getEntityManager();
    protected abstract Class<T> getTypeParameterClass();

    /**
     * Tworzy obiekt encji w bazie danych.
     * @param entity Obiekt encji
     */
    @Override
    public T create(T entity) {
        getEntityManager().persist(entity);
        getEntityManager().flush();
        return entity;
    }

    /**
     * Wykonuje merge na istniejącym obiekcie encji.
     * @param entity Obiekt encji
     */
    @Override
    public T edit(T entity) throws EntityUpdateException {
        try {
            getEntityManager().merge(entity);
            getEntityManager().flush();
            return entity;
        } catch(OptimisticLockException e) {
            throw new EntityUpdateException("Entity has been updated before these changes were made", e);
        }
    }
    
    /**
     * Edycja istniejącej encji bez wykonywania operacji merge.
     * @param entity Obiekt encji
     * @return encja po edycji
     * @throws EntityUpdateException rzucony, gdy edycja się nie powiedzie
     */
    @Override
    public T editWithoutMerge(T entity) throws EntityUpdateException {
        try {
            getEntityManager().flush();
            return entity;
        } catch(OptimisticLockException e) {
            throw new EntityUpdateException("Entity has been updated before these changes were made", e);
        }
    }

    /**
     * Znajduje obiekt encji na podstawie podanego identyfikatora.
     * @param id Identyfikator encji
     * @return Obiekt encji opakowany w obiekt klasy Optional
     */
    @Override
    public Optional<T> findById(ID id) {
        T retrievedObject = getEntityManager().find(getTypeParameterClass(), id);

        if (retrievedObject == null){
            return Optional.empty();
        }
        return Optional.of(retrievedObject);
    }
    /**
     * Sprawdza czy obiekt istnieje w bazie danych.
     * @param id Identyfikator encji
     * @return true w przypadku, gdy obiekt istnieje w magazynie danych, w przeciwnym wypadku false
            */
    @Override
    public boolean existsById(ID id) {
        Optional<T> retrieved = findById(id);
        return retrieved.isPresent();
    }

    /**
     * Zwraca listę wszystkich encji w bazie danych.
     * @return Lista wszystkich encji
     */
    @Override
    public List<T> findAll() {
        CriteriaQuery<T> cq = getEntityManager().getCriteriaBuilder().createQuery(getTypeParameterClass());
        cq.select(cq.from(getTypeParameterClass()));
        return getEntityManager().createQuery(cq).getResultList();
    }

    /**
     * Odświeża obiekt encji w kontekście EntityManager.
     * @param entity obiekt encji, który należy odświeżyć.
     * @return odświeżony obiekt encji.
     */
    @Override
    public T refresh(T entity) {
        getEntityManager().refresh(entity);
        return entity;
    }

    /**
     * Tworzy TypedQuery dla zadanego NamedQuery. Metoda pomocnicza dla klas dziedziczących.
     * @param queryName Nazwa NamedQuery dla encji
     * @return TypedQuery, zapytanie dla podanej nazwy NamedQuery
     */
    protected TypedQuery<T> createNamedQuery(String queryName) {
        return this.createNamedQuery(queryName, getTypeParameterClass());
    }

    /**
     * Tworzy TypedQuery na podstawie utworzonego NamedQuery.
     * @param name Nazwa dla NamedQuery do utworzenia
     * @param resultClass Typ klasy wyjściowej
     * @return TypedQuery dla zadanych parametrów
     */
    protected TypedQuery<T> createNamedQuery(String name, Class<T> resultClass) {
        return getEntityManager().createNamedQuery(name, resultClass);
    }

    /**
     * Wykonuje zapytanie modyfikujące na bazie danych.
	 * Metoda pomocnicza dla klas dziedziczących.
     * @param query Zapytanie JPQL
     * @return Wynik zapytania w postaci kodu wyniku
     */
    protected int executeUpdateQuery(String query) {
        return getEntityManager().createQuery(query).executeUpdate();
    }

    /**
     * Wykonuje zapytanie modyfikujące na bazie danych wykorzystując istniejące NamedQuery.
     * Metoda pomocnicza dla klas dziedziczących.
     * @param name Nazwa NamedQuery dla encji
     * @return Wynik zapytania w postaci kodu
     */
    protected int executeUpdateNamedQuery(String name) {
        return getEntityManager().createNamedQuery(name).executeUpdate();
    }

    /**
     * Tworzy obiekt reprezentujący zapytanie. Tylko dla zapytań niemodyfikujących.
     * Metoda pomocnicza dla klas dziedziczących.
     * @param query Zapytanie w formie JPQL
     * @return Obiekt query reprezentujący zapytanie
     */
    protected Query createQuery(String query) {
        return getEntityManager().createQuery(query);
    }

}
