package pl.lodz.p.it.ssbd2019.ssbd03.mok.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.ConfirmationToken;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.DataAccessException;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.AbstractCruRepository;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Stateless(name = "MOKConfirmationTokenRepository")
@DenyAll
public class ConfirmationTokenRepositoryLocalImpl extends AbstractCruRepository<ConfirmationToken, Long>
        implements ConfirmationTokenRepositoryLocal {

    @PersistenceContext(unitName = "ssbd03mokPU")
    private EntityManager entityManager;

    @Override
    @PermitAll
    public Optional<ConfirmationToken> findByToken(String token) {
        TypedQuery<ConfirmationToken> namedQuery = this.createNamedQuery("ConfirmationToken.findByToken");
        namedQuery.setParameter("token", token);
        return Optional.of(namedQuery.getSingleResult());
    }

    @Override
    @PermitAll
    public ConfirmationToken create(ConfirmationToken confirmationToken) throws DataAccessException {
        return super.create(confirmationToken);
    }

    @Override
    @PermitAll
    public void edit(ConfirmationToken confirmationToken) throws DataAccessException {
        super.edit(confirmationToken);
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    protected Class<ConfirmationToken> getTypeParameterClass() {
        return ConfirmationToken.class;
    }
}
