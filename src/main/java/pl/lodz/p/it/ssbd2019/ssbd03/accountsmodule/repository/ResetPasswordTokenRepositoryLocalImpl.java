package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.ResetPasswordToken;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.AbstractCruRepository;

import javax.ejb.Stateless;
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
    public Optional<ResetPasswordToken> findByToken(String token) {
        TypedQuery<ResetPasswordToken> namedQuery = this.createNamedQuery("ResetPasswordToken.findByToken");
        namedQuery.setParameter("token", token);
        return Optional.of(namedQuery.getSingleResult());
    }
}
