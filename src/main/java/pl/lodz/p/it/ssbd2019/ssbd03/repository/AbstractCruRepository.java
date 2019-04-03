package pl.lodz.p.it.ssbd2019.ssbd03.repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Optional;

public abstract class AbstractCruRepository<T, ID> implements CruRepository<T, ID> {

    protected abstract EntityManager getEntityManager();
    protected abstract Class<T> getTypeParameterClass();

    @Override
    public void create(T entity) {
        getEntityManager().persist(entity);
        getEntityManager().flush();
    }

    @Override
    public void edit(T entity) {
        getEntityManager().merge(entity);
        getEntityManager().flush();
    }

    @Override
    public Optional<T> findById(ID id) {
        T retrievedObject = getEntityManager().find(getTypeParameterClass(), id);

        if (retrievedObject == null){
            return Optional.empty();
        }
        return Optional.of(retrievedObject);
    }

    @Override
    public boolean existsById(ID id) {
        Optional<T> retrieved = findById(id);
        return retrieved.isPresent();
    }

    @Override
    public List<T> findAll() {
        CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(getTypeParameterClass()));
        return getEntityManager().createQuery(cq).getResultList();
    }

    protected TypedQuery<T> createNamedQuery(String queryName) {
        return this.createNamedQuery(queryName, getTypeParameterClass());
    }

    protected TypedQuery<T> createNamedQuery(String name, Class<T> resultClass) {
        return getEntityManager().createNamedQuery(name, resultClass);
    }

    protected int executeUpdateQuery(String query) {
        return getEntityManager().createQuery(query).executeUpdate();
    }

    protected int executeUpdateNamedQuery(String name) {
        return getEntityManager().createNamedQuery(name).executeUpdate();
    }

    protected Query createQuery(String query) {
        return getEntityManager().createQuery(query);
    }

}
