package pl.lodz.p.it.ssbd2019.ssbd03.mor.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.AbstractCruRepository;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Stateless
@DenyAll
public class UserAccountRepositoryImpl extends AbstractCruRepository<UserAccount, Long> implements UserAccountRepositoryLocal {

    @PersistenceContext(unitName = "ssbd03morPU")
    private EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    protected Class<UserAccount> getTypeParameterClass() {
        return UserAccount.class;
    }

    @Override
    @RolesAllowed("GetReservationsForUser")
    public Optional<UserAccount> findByLogin(String login) {
        TypedQuery<UserAccount> namedQuery = this.createNamedQuery("UserAccount.findByLogin");
        namedQuery.setParameter("login", login);
        return Optional.of(namedQuery.getSingleResult());
    }
}
