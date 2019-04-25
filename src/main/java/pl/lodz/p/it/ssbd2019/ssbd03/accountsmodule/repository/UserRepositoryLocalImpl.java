package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.User;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.AbstractCruRepository;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Stateless(name = "MOKUserRepository")
public class UserRepositoryLocalImpl extends AbstractCruRepository<User, Long> implements UserRepositoryLocal {
    @PersistenceContext(unitName = "ssbd03mokPU")
    private EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    protected Class<User> getTypeParameterClass() {
        return User.class;
    }

    @Override
    public Optional<User> findByLogin(String login) {
        TypedQuery<User> namedQuery = this.createNamedQuery("User.findByLogin");
        namedQuery.setParameter("login", login);
        return Optional.of(namedQuery.getSingleResult());
    }
}
