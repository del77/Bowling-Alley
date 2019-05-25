package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository;

import java.util.*;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.EntityUpdateException;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.AbstractCruRepository;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MokRoles;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
    @PermitAll
    public Optional<UserAccount> findByLogin(String login) {
        TypedQuery<UserAccount> namedQuery = this.createNamedQuery("UserAccount.findByLogin");
        namedQuery.setParameter("login", login);
        return Optional.of(namedQuery.getSingleResult());
    }


    @Override
    @PermitAll
    public Optional<UserAccount> findByEmail(String email) {
        TypedQuery<UserAccount> namedQuery = this.createNamedQuery("UserAccount.findByEmail");
        namedQuery.setParameter("email", email);
        return Optional.of(namedQuery.getSingleResult());
    }

    @Override
    @PermitAll
    public Optional<UserAccount> findById(Long id) {
        return super.findById(id);
    }

    @Override
    @PermitAll
    public UserAccount create(UserAccount userAccount) {
        return super.create(userAccount);
    }

    @Override
    @PermitAll
    public UserAccount edit(UserAccount userAccount) throws EntityUpdateException {
        return super.edit(userAccount);
    }

    @Override
    @RolesAllowed(MokRoles.GET_ALL_USERS_LIST)
    public List<UserAccount> findAll() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserAccount> query = builder.createQuery(UserAccount.class);
        Root<UserAccount> root = query.from(UserAccount.class);
        query.orderBy(builder.asc(root.get("login")));
        return entityManager.createQuery(query).getResultList();
    }
}
