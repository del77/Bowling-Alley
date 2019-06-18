package pl.lodz.p.it.ssbd2019.ssbd03.mot.web;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.ServiceRequest;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.DataAccessException;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.service.AlleyService;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.service.ServiceRequestService;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.web.dto.ServiceRequestDto;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.web.dto.ServiceRequestViewDto;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.DtoValidator;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.ValidatorConfig;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.localization.LocalizedMessageProvider;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.redirect.FormData;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.redirect.RedirectUtil;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MotRoles;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.mvc.Controller;
import javax.mvc.Models;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;

@SessionScoped
@Controller
@Path("employee/servicerequests")
public class ServiceRequestController implements Serializable {
    private static final String ERROR = "errors";
    private static final String INFO = "infos";
    private static final String ADD_SERVICE_REQUEST_PAGE = "mot/sr/addsr.hbs";
    private static final String SERVICE_REQUEST_ENDPOINT_PATTERN = "employee/servicerequests/new/%d/";
    public static final String ALLEYS_LIST_VIEW = "mot/sr/srList.hbs";

    private Long alleyIdToFetch;

    private String alleyNumber;

    @Inject
    private Models models;

    @Inject
    private LocalizedMessageProvider localization;

    @EJB
    private ServiceRequestService serviceRequestService;

    @EJB
    private AlleyService alleyService;

    @Inject
    private DtoValidator dtoValidator;

    @Inject
    private RedirectUtil redirectUtil;

    /**
     * Zwraca widok pozwalający dodać nowe zgłoszenie serwisowe
     *
     * @return Widok z formularzem pozwalającym dodać zgłoszenie-
     */
    @GET
    @Path("new/{alleyId}")
    @RolesAllowed(MotRoles.ADD_SERVICE_REQUEST)
    @Produces(MediaType.TEXT_HTML)
    public String addServiceRequest(@QueryParam("idCache") Long idCache,
                                    @PathParam("alleyId") Long alleyId) {
        redirectUtil.injectFormDataToModels(idCache, models);
        if (alleyId == null) {
            displayError(localization.get("alleyIdNull"));
        }
        Integer alleyNumber = null;
        try {
            alleyNumber = this.alleyService.getById(alleyId).getNumber();
        } catch (SsbdApplicationException e) {
            displayError(localization.get("alleyNotFound"));
        }
        models.put("alleyNumber", alleyNumber);
        models.put("alleyId", alleyId);
        return ADD_SERVICE_REQUEST_PAGE;
    }

    /**
     * Dodaje nowe zgłoszenie serwisowe
     *
     * @param serviceRequest obiekt zawierający informacje o zgłoszeniu serwisowym
     */
    @POST
    @Path("new/{alleyId}")
    @RolesAllowed(MotRoles.ADD_SERVICE_REQUEST)
    @Produces(MediaType.TEXT_HTML)
    public String addServiceRequest(@BeanParam ServiceRequestDto serviceRequest, @PathParam("alleyId") Long alleyId) {
        List<String> validateResult = dtoValidator.validate(serviceRequest);
        if (validateResult != null && !validateResult.isEmpty()) {
            return redirectUtil.redirect(
                    String.format(SERVICE_REQUEST_ENDPOINT_PATTERN, alleyId),
                    new FormData(serviceRequest, validateResult,
                            Collections.emptyList())
            );
        }
        if (alleyId != null) {
            try {
                serviceRequestService.addServiceRequest(alleyId, serviceRequest.getContent());
            } catch (SsbdApplicationException e) {
                return redirectUtil.redirect(
                        String.format(SERVICE_REQUEST_ENDPOINT_PATTERN, alleyId),
                        new FormData(serviceRequest, Collections.singletonList(localization.get(e.getCode())),
                                Collections.emptyList())
                );
            }
        }
        return redirectUtil.redirect(
                String.format(SERVICE_REQUEST_ENDPOINT_PATTERN, alleyId),
                new FormData(
                        null,
                        Collections.emptyList(),
                        Collections.singletonList(localization.get("serviceRequestAdditionSuccess"))
                )
        );
    }

    /**
     * Zwraca widok pozwalający edytować zgłoszenie serwisowe
     *
     * @param id identyfikator zgłoszenia serwisowego
     * @return Widok z formularzem wypełnionym aktualnymi danymi
     */
    @GET
    @Path("{id}/edit")
    @RolesAllowed(MotRoles.EDIT_SERVICE_REQUEST)
    @Produces(MediaType.TEXT_HTML)
    public String editServiceRequest(@PathParam("id") Long id) {
        throw new UnsupportedOperationException();
    }

    /**
     * Aktualizuje zgłoszenie serwisowe
     *
     * @param serviceRequest obiekt zawierający informacje o zaktualizowanym zgłoszeniu serwisowym
     */
    @POST
    @Path("edit")
    @RolesAllowed(MotRoles.EDIT_SERVICE_REQUEST)
    @Produces(MediaType.TEXT_HTML)
    public String editServiceRequest(@BeanParam ServiceRequest serviceRequest) {
        throw new UnsupportedOperationException();
    }

    /**
     * Pobiera zgłoszenia serwisowe
     *
     * @return Widok ze zgłoszeniami serwisowymi
     */
    @GET
    @RolesAllowed(MotRoles.GET_SERVICE_REQUESTS)
    @Produces(MediaType.TEXT_HTML)
    public String getServiceRequests() {
        List<ServiceRequest> serviceRequests = new ArrayList<>();
        try {
            serviceRequests = this.serviceRequestService.getAllServiceRequests();
        } catch (DataAccessException e) {
            displayError(localization.get("serviceRequestRetrievalError"));
        }
        models.put("srs", this.mapToViewDto(serviceRequests));
        return ALLEYS_LIST_VIEW;
    }

    private List<ServiceRequestViewDto> mapToViewDto(List<ServiceRequest> serviceRequests) {
        List<ServiceRequestViewDto> viewDtos = new ArrayList<>();
        for (ServiceRequest sr : serviceRequests) {
            viewDtos.add(
                    ServiceRequestViewDto
                            .builder()
                            .id(sr.getId())
                            .alleyNumber((sr.getAlley() != null ? sr.getAlley().getNumber() : -1))
                            .content(sr.getContent())
                            .userLogin((sr.getUserAccount() != null ? sr.getUserAccount().getLogin() : ""))
                            .resolved(sr.isResolved())
                            .build()
            );
        }
        return viewDtos;
    }

    private void displayError(String s) {
        models.put(ERROR, Collections.singletonList(s));
    }

    private void displayErrors(List<String> s) {
        models.put(ERROR, s);
    }

    private void displayInfo(String s) {
        models.put(INFO, s);
    }
}
