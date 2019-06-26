package pl.lodz.p.it.ssbd2019.ssbd03.mot.repository;

import org.postgresql.util.PSQLException;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.Alley;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.*;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.AbstractCruRepository;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MotRoles;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

@Stateless(name = "MOTAlleyRepository")
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@DenyAll
public class AlleyRepositoryLocalImpl extends AbstractCruRepository<Alley, Long> implements AlleyRepositoryLocal {

    @PersistenceContext(unitName = "ssbd03motPU")
    private EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    protected Class<Alley> getTypeParameterClass() {
        return Alley.class;
    }

    @Override
    @RolesAllowed(MotRoles.GET_ALLEYS_LIST)
    public List<Alley> findAll() throws DataAccessException {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Alley> query = builder.createQuery(Alley.class);
            Root<Alley> root = query.from(Alley.class);
            query.orderBy(builder.asc(root.get("number")));
            return entityManager.createQuery(query).getResultList();
        } catch (PersistenceException e) {
            throw new EntityRetrievalException("Could not retrieve alleys", e);
        }
    }

    @Override
    @RolesAllowed(MotRoles.ENABLE_DISABLE_ALLEY)
    public void edit(Alley alley) throws DataAccessException {
        super.edit(alley);
    }

    @Override
    @PermitAll
    public Alley editWithoutMerge(Alley alley) throws DataAccessException {
        try {
            return super.editWithoutMerge(alley);
        } catch (OptimisticLockException e) {
            throw new AlleyOptimisticLockException("Alley has been updated before these changes were made", e);
        } catch (PersistenceException e) {
            throw new EntityUpdateException("Could not perform update operation.", e);
        } catch (ConstraintViolationException e) {
            if(e.getMessage().toLowerCase().contains("number"))
                throw new AlleyNumberLessThanOneException("Number less than one", e);
            else throw new AlleyScoreConstraintViolationException("Wrong alley score", e);
        }
    }

    @Override
    @RolesAllowed({MotRoles.GET_ALLEY_GAMES_HISTORY, MotRoles.GET_BEST_SCORE_FOR_ALLEY, MotRoles.ADD_SERVICE_REQUEST})
    public Optional<Alley> findById(Long id) throws DataAccessException {
        return super.findById(id);
    }

    @Override
    @RolesAllowed(MotRoles.ADD_ALLEY)
    public Alley create(Alley alley) throws DataAccessException {
        try {
            return super.create(alley);
        } catch (ConstraintViolationException e) {
            if (e.getMessage().contains("number")) {
                throw new AlleyNumberLessThanOneException(String.format("Given alley number %d is invalid.", alley.getNumber()));
            }
            throw new DatabaseConstraintViolationException("Violated constraint during alley creation", e);
        } catch (PersistenceException e) {
            Throwable t = e.getCause();
            Throwable t2 = t.getCause();
            if ((t2 instanceof PSQLException) && t2.getMessage().contains("number")) {
                throw new NotUniqueAlleyNumberException(String.format("There is an alley with the number %d already.",alley.getNumber()), e);
            } else {
                throw new EntityUpdateException("Could not perform create operation.", e);
            }
        }
    }
}
