package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.AbstractCruRepository;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;


@Stateless(name = "MOKUserRepository")
@DenyAll
public class UserAccountRepositoryLocalImpl extends AbstractCruRepository<UserAccount, Long> implements UserAccountRepositoryLocal {
    @PersistenceContext(unitName = "ssbd03mokPU")
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
    @RolesAllowed("ChangeOwnPassword")
    public Optional<UserAccount> findByLogin(String login) {
        TypedQuery<UserAccount> namedQuery = this.createNamedQuery("UserAccount.findByLogin");
        namedQuery.setParameter("login", login);
        return Optional.of(namedQuery.getSingleResult());
    }

    @Override
    @PermitAll
    public Optional<UserAccount> findById(Long id)
    {
        return super.findById(id);
    }

    @Override
    @PermitAll
    public UserAccount create(UserAccount userAccount)
    {
        return super.create(userAccount);
    }

    @Override
    @RolesAllowed("GetAllUsersList")
    public List<UserAccount> findAll() {
        return super.findAll();
    }

    @Override
    @PermitAll
    public UserAccount edit(UserAccount userAccount) {
        return super.edit(userAccount);
    }


}
