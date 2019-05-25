package pl.lodz.p.it.ssbd2019.ssbd03.mot.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.ServiceRequest;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.EntityUpdateException;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.AbstractCruRepository;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MotRoles;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
@DenyAll
public class ServiceRequestRepositoryImpl extends AbstractCruRepository<ServiceRequest, Long> implements ServiceRequestRepositoryLocal {

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
    public ServiceRequest create(ServiceRequest serviceRequest) {
        return super.create(serviceRequest);
    }

    @Override
    @RolesAllowed(MotRoles.EDIT_SERVICE_REQUEST)
    public ServiceRequest edit(ServiceRequest serviceRequest) throws EntityUpdateException {
        return super.edit(serviceRequest);
    }

    @Override
    @RolesAllowed(MotRoles.GET_SERVICE_REQUESTS)
    public List<ServiceRequest> findAll() {
        return super.findAll();
    }
}
