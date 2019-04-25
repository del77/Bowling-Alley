package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.AbstractCruRepository;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Stateless(name = "MOKAccountRepository")
public class AccountRepositoryLocalImpl extends AbstractCruRepository<Account, Long> implements AccountRepositoryLocal {
    @PersistenceContext(unitName = "ssbd03mokPU")
    private EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    protected Class<Account> getTypeParameterClass() {
        return Account.class;
    }

    @Override
    public Optional<Account> findByLogin(String login) {
        TypedQuery<Account> namedQuery = this.createNamedQuery("Account.findByLogin");
        namedQuery.setParameter("login", login);
        return Optional.of(namedQuery.getSingleResult());
    }
}
