package pl.lodz.p.it.ssbd2019.ssbd03.mok.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.ResetPasswordToken;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.AbstractCruRepository;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Stateless(name = "MOKResetPasswordTokenRepository")
public class ResetPasswordTokenRepositoryLocalImpl extends AbstractCruRepository<ResetPasswordToken, Long> implements ResetPasswordTokenRepositoryLocal {
    @PersistenceContext(unitName = "ssbd03mokPU")
    private EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    protected Class<ResetPasswordToken> getTypeParameterClass() {
        return ResetPasswordToken.class;
    }

    @Override
    @PermitAll
    public Optional<ResetPasswordToken> findByToken(String token) {
        try {
            TypedQuery<ResetPasswordToken> namedQuery = this.createNamedQuery("ResetPasswordToken.findByToken");
            namedQuery.setParameter("token", token);
            return Optional.of(namedQuery.getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
