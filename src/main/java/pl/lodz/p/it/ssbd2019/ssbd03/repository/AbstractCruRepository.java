package pl.lodz.p.it.ssbd2019.ssbd03.repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

public abstract class AbstractCruRepository<T,ID> implements CruRepository<T, ID> {

    protected abstract EntityManager getEntityManager();
    protected abstract String getTableName();

    private final Class<T> typeParameterClass;

    public AbstractCruRepository(Class<T> typeParameterClass) {
        this.typeParameterClass = typeParameterClass;
    }

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
        T retrievedObject = getEntityManager().find(typeParameterClass, id);

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
        String select = String.format("SELECT * FROM %s", getTableName());
        Query query = getEntityManager().createQuery(select);
        return query.getResultList();
    }

}
