package pl.lodz.p.it.ssbd2019.ssbd03.mot.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.ServiceRequest;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.*;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.AbstractCruRepository;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MotRoles;

import javax.annotation.security.DenyAll;
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

@Stateless(name = "MOTServiceRequestRepository")
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@DenyAll
public class ServiceRequestRepositoryLocalImpl extends AbstractCruRepository<ServiceRequest, Long> implements ServiceRequestRepositoryLocal {

    @PersistenceContext(unitName = "ssbd03motPU")
    private EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    protected Class<ServiceRequest> getTypeParameterClass() {
        return ServiceRequest.class;
    }

    @Override
    @RolesAllowed(MotRoles.ADD_SERVICE_REQUEST)
    public ServiceRequest create(ServiceRequest serviceRequest) throws DataAccessException {
        try {
            super.create(serviceRequest);
            return serviceRequest;
        } catch (ConstraintViolationException e) {
            throw new DatabaseConstraintViolationException(e);
        } catch (PersistenceException e) {
            throw new EntityUpdateException(e);
        }
    }

    @Override
    @RolesAllowed(MotRoles.EDIT_SERVICE_REQUEST)
    public void edit(ServiceRequest serviceRequest) throws DataAccessException {
        try {
            super.edit(serviceRequest);
        } catch (OptimisticLockException e) {
            throw new ServiceRequestOptimisticLockException(e);
        } catch (ConstraintViolationException e) {
            throw new DatabaseConstraintViolationException(e);
        } catch (PersistenceException e) {
            throw new EntityUpdateException(e);
        }
    }

    @Override
    @RolesAllowed(MotRoles.GET_SERVICE_REQUESTS)
    public List<ServiceRequest> findAll() throws DataAccessException {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<ServiceRequest> query = builder.createQuery(ServiceRequest.class);
            Root<ServiceRequest> root = query.from(ServiceRequest.class);
            query.orderBy(builder.asc(root.get("id")), builder.asc(root.get("resolved")));
            return entityManager.createQuery(query).getResultList();
        } catch (Exception e) {
            throw new EntityRetrievalException(e);
        }
    }
}
