package pl.lodz.p.it.ssbd2019.ssbd03.reservationsmodule.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.AbstractCruRepository;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class UserRepositoryImpl extends AbstractCruRepository<UserAccount, Integer> implements UserRepositoryLocal {

    @PersistenceContext(unitName = "ssbd03morPU")
    private EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    protected Class getTypeParameterClass() {
        return UserAccount.class;
    }
}
