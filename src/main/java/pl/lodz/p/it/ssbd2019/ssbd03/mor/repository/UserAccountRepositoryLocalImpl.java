package pl.lodz.p.it.ssbd2019.ssbd03.mor.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.AbstractCruRepository;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MorRoles;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;
import javax.persistence.PersistenceException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.EntityRetrievalException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.LoginDoesNotExistException;

@Stateless(name = "MORUserAccountRepository")
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@DenyAll
public class UserAccountRepositoryLocalImpl extends AbstractCruRepository<UserAccount, Long> implements UserAccountRepositoryLocal {

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
    @RolesAllowed({MorRoles.GET_RESERVATIONS_FOR_USER, MorRoles.GET_OWN_RESERVATIONS, MorRoles.EDIT_OWN_RESERVATION, MorRoles.CREATE_RESERVATION, MorRoles.CREATE_RESERVATION_FOR_USER})
    public Optional<UserAccount> findByLogin(String login) throws EntityRetrievalException {
        try {
            TypedQuery<UserAccount> namedQuery = this.createNamedQuery("UserAccount.findByLogin");
            namedQuery.setParameter("login", login);
            return Optional.of(namedQuery.getSingleResult());
        } catch (PersistenceException e) {
            throw new LoginDoesNotExistException("Could not find entity with given login", e);
        } catch (Exception e) {
            throw new EntityRetrievalException();
        }
    }
}
