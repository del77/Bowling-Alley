package pl.lodz.p.it.ssbd2019.ssbd03.mot.service;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.ServiceRequest;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MotRoles;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.List;

@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Stateful
@DenyAll
public class ServiceRequestServiceImpl implements ServiceRequestService {
    @Override
    @RolesAllowed(MotRoles.ADD_SERVICE_REQUEST)
    public void addServiceRequest(ServiceRequest serviceRequest) {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed(MotRoles.EDIT_SERVICE_REQUEST)
    public void updateServiceRequest(ServiceRequest serviceRequest) {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed(MotRoles.GET_SERVICE_REQUESTS)
    public List<ServiceRequest> getAllServiceRequests() {
        throw new UnsupportedOperationException();
    }
}
