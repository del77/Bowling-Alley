package pl.lodz.p.it.ssbd2019.ssbd03.mot.service;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Alley;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.ServiceRequest;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.DataAccessException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.notfound.NotFoundException;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.repository.AlleyRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.repository.ServiceRequestRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.repository.UserAccountRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MotRoles;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Stateful
@DenyAll
public class ServiceRequestServiceImpl implements ServiceRequestService {

    @EJB(beanName = "MOTServiceRequestRepository")
    private ServiceRequestRepositoryLocal serviceRequestRepositoryLocal;

    @EJB(beanName = "MOTUserRepository")
    private UserAccountRepositoryLocal userAccountRepositoryLocal;

    @EJB(beanName = "MOTAlleyRepository")
    private AlleyRepositoryLocal alleyRepositoryLocal;

    @Inject
    private SecurityContext securityContext;

    @Override
    @RolesAllowed(MotRoles.ADD_SERVICE_REQUEST)
    public void addServiceRequest(Long alleyId, String content) throws SsbdApplicationException {
        Optional<Alley> alleyById = alleyRepositoryLocal.findById(alleyId);
        Optional<UserAccount> userByLogin =
                userAccountRepositoryLocal.findByLogin(securityContext.getCallerPrincipal().getName());
        Alley alley = alleyById.orElseThrow(NotFoundException::new);
        UserAccount user = userByLogin.orElseThrow(NotFoundException::new);
        ServiceRequest serviceRequest = ServiceRequest
                .builder()
                .alley(alley)
                .date(Timestamp.valueOf(LocalDateTime.now()))
                .userAccount(user)
                .content(content)
                .resolved(false)
                .build();
        serviceRequestRepositoryLocal.create(serviceRequest);
    }

    @Override
    @RolesAllowed(MotRoles.EDIT_SERVICE_REQUEST)
    public void updateServiceRequest(ServiceRequest serviceRequest) {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed(MotRoles.GET_SERVICE_REQUESTS)
    public List<ServiceRequest> getAllServiceRequests() throws DataAccessException {
        return this.serviceRequestRepositoryLocal.findAll();
    }
}
