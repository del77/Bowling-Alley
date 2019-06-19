package pl.lodz.p.it.ssbd2019.ssbd03.mot.service;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Alley;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.ServiceRequest;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.notfound.NotFoundException;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.repository.AlleyRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.repository.ServiceRequestRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.repository.UserAccountRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.web.dto.ServiceRequestEditDto;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.web.dto.ServiceRequestViewDto;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MotRoles;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Stateful(name = "MOTServiceRequestService")
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
    public void updateServiceRequest(ServiceRequestEditDto serviceRequest) throws SsbdApplicationException {
        Optional<ServiceRequest> byId = this.serviceRequestRepositoryLocal.findById(serviceRequest.getId());
        ServiceRequest sr = byId.orElseThrow(NotFoundException::new);
        sr.setResolved(serviceRequest.getResolved());
        sr.setContent(serviceRequest.getContent());
        this.serviceRequestRepositoryLocal.edit(sr);
    }

    @Override
    @RolesAllowed(MotRoles.GET_SERVICE_REQUESTS)
    public List<ServiceRequestViewDto> getAllServiceRequests() throws SsbdApplicationException {
        return this.mapToViewDto(this.serviceRequestRepositoryLocal.findAll());
    }

    @Override
    @RolesAllowed(MotRoles.EDIT_SERVICE_REQUEST)
    public ServiceRequestViewDto getById(Long id) throws SsbdApplicationException {
        return this.mapSingle(this.serviceRequestRepositoryLocal.findById(id).orElseThrow(NotFoundException::new));
    }

    private List<ServiceRequestViewDto> mapToViewDto(List<ServiceRequest> serviceRequests) {
        List<ServiceRequestViewDto> viewDtos = new ArrayList<>();
        for (ServiceRequest sr : serviceRequests) {
            viewDtos.add(
                    this.mapSingle(sr)
            );
        }
        return viewDtos;
    }

    private ServiceRequestViewDto mapSingle(ServiceRequest sr) {
        return ServiceRequestViewDto
                        .builder()
                        .id(sr.getId())
                        .alleyNumber((sr.getAlley() != null ? sr.getAlley().getNumber() : -1))
                        .content(sr.getContent())
                        .userLogin((sr.getUserAccount() != null ? sr.getUserAccount().getLogin() : ""))
                        .resolved(sr.isResolved())
                        .build();
    }
}
