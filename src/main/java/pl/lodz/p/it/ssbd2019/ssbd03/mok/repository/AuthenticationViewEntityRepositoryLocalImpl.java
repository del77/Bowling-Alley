package pl.lodz.p.it.ssbd2019.ssbd03.mok.repository;

import java.util.List;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.AuthenticationViewEntity;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.AuthenticationViewEntityId;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.AbstractCruRepository;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Stateless(name = "MOKAuthenticationViewEntityRepositoryLocalImpl")
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@DenyAll
public class AuthenticationViewEntityRepositoryLocalImpl
        extends AbstractCruRepository<AuthenticationViewEntity, AuthenticationViewEntityId>
        implements AuthenticationViewEntityRepositoryLocal {
    
    @PersistenceContext(unitName = "ssbd03authPU")
    private EntityManager entityManager;
    
    @PermitAll
    public List<AuthenticationViewEntity> findAllWithLogin(String login) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<AuthenticationViewEntity> query = builder.createQuery(AuthenticationViewEntity.class);
        Root<AuthenticationViewEntity> root = query.from(AuthenticationViewEntity.class);
        query.select(root).where(builder.equal(root.get("login"), login));
        return entityManager.createQuery(query).getResultList();
    }
    
    @PermitAll
    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }
    
    @PermitAll
    @Override
    protected Class<AuthenticationViewEntity> getTypeParameterClass() {
        return AuthenticationViewEntity.class;
    }
}
