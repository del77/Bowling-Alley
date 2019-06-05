package pl.lodz.p.it.ssbd2019.ssbd03.mot.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.DataAccessException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.LoginDoesNotExistException;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.AbstractCruRepository;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MotRoles;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Stateless(name = "MOTUserRepository")
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@DenyAll
public class UserAccountRepositoryLocalImpl extends AbstractCruRepository<UserAccount, Long> implements UserAccountRepositoryLocal {

    @PersistenceContext(unitName = "ssbd03motPU")
    private EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    @Override
    protected Class<UserAccount> getTypeParameterClass() {
        return UserAccount.class;
    }

    @Override
    @RolesAllowed(MotRoles.ADD_SCORE)
    public Optional<UserAccount> findByLogin(String login) throws DataAccessException {
        try {
            TypedQuery<UserAccount> namedQuery = this.createNamedQuery("UserAccount.findByLogin");
            namedQuery.setParameter("login", login);
            return Optional.of(namedQuery.getSingleResult());
        } catch (PersistenceException e) {
            throw new LoginDoesNotExistException("Could not find entity with given login", e);
        }
    }

    @Override
    @RolesAllowed({MotRoles.SHOW_USER_SCORE_HISTORY})
    public Optional<UserAccount> findById(Long id) throws DataAccessException {
        return super.findById(id);
    }
}
