package pl.lodz.p.it.ssbd2019.ssbd03.mok.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccessLevel;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.AbstractCruRepository;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;

@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Stateless(name = "MOKAccessLevelRepository")
@DenyAll
public class AccessLevelRepositoryLocalImpl extends AbstractCruRepository<AccessLevel, Long> implements AccessLevelRepositoryLocal {
    @PersistenceContext(unitName = "ssbd03mokPU")
    private EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    protected Class<AccessLevel> getTypeParameterClass() {
        return AccessLevel.class;
    }

    @Override
    @PermitAll
    public Optional<AccessLevel> findByName(String name) {
        TypedQuery<AccessLevel> query = this.createNamedQuery("AccessLevel.findByName");
        query.setParameter("name", name);
        return Optional.of(query.getSingleResult());
    }
}
