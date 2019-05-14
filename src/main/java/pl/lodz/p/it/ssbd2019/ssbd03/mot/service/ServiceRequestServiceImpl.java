package pl.lodz.p.it.ssbd2019.ssbd03.mot.service;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.ServiceRequest;

import javax.annotation.security.RolesAllowed;
import java.util.List;

public class ServiceRequestServiceImpl implements ServiceRequestService {
    @Override
    @RolesAllowed("AddServiceRequest")
    public void addServiceRequest(ServiceRequest serviceRequest) {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed("EditServiceRequest")
    public void updateServiceRequest(ServiceRequest serviceRequest) {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed("GetServiceRequests")
    public List<ServiceRequest> getAllServiceRequests() {
        throw new UnsupportedOperationException();
    }
}
