package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccessLevel;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccountAccessLevel;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccountAccessLevelId;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.AbstractCruRepository;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class AccountAccessLevelRepositoryLocalImpl extends AbstractCruRepository<AccountAccessLevel, AccountAccessLevelId> implements AccountAccessLevelRepositoryLocal {
    @PersistenceContext(unitName = "ssbd03mokPU")
    private EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    protected Class<AccountAccessLevel> getTypeParameterClass() {
        return AccountAccessLevel.class;
    }

    @Override
    public List<AccountAccessLevel> findAllForAccount(Account account) {
        TypedQuery<AccountAccessLevel> namedQuery = this.createNamedQuery("AccountAccessLevel.findForAccountId");
        namedQuery.setParameter("account", account);
        return namedQuery.getResultList();
    }

    @Override
    public List<AccountAccessLevel> findAllForAccessLevel(AccessLevel accessLevel) {
        TypedQuery<AccountAccessLevel> namedQuery = this.createNamedQuery("AccountAccessLevel.findForAccessLevelId");
        namedQuery.setParameter("access", accessLevel);
        return namedQuery.getResultList();
    }
}
