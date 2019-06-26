package pl.lodz.p.it.ssbd2019.ssbd03.mot.web;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.service.AlleyService;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.service.ServiceRequestService;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.web.dto.ServiceRequestDto;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.web.dto.ServiceRequestEditDto;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.web.dto.ServiceRequestViewDto;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.DtoValidator;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.helpers.Mapper;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.localization.LocalizedMessageProvider;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.redirect.FormData;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.redirect.RedirectUtil;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MotRoles;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.mvc.Controller;
import javax.mvc.Models;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SessionScoped
@Controller
@Path("employee/servicerequests")
public class ServiceRequestController implements Serializable {
    private static final String ERROR = "errors";
    private static final String INFO = "infos";
    private static final String ADD_SERVICE_REQUEST_PAGE = "mot/sr/addsr.hbs";
    private static final String SERVICE_REQUEST_ENDPOINT_PATTERN = "employee/servicerequests/new/%d/";
    private static final String EDIT_SERVICE_REQUEST_PAGE = "mot/sr/editsr.hbs";
    private static final String SERVICE_REQUEST_EDIT_ENDPOINT_PATTERN = "employee/servicerequests/edit/%d";
    private static final String ALLEYS_LIST_VIEW = "mot/sr/srList.hbs";

    @Inject
    private Models models;

    @Inject
    private LocalizedMessageProvider localization;

    @EJB(beanName = "MOTServiceRequestService")
    private ServiceRequestService serviceRequestService;

    @EJB(beanName = "MOTAlleyService")
    private AlleyService alleyService;

    @Inject
    private DtoValidator dtoValidator;

    @Inject
    private RedirectUtil redirectUtil;

    /**
     * Zwraca widok pozwalający dodać nowe zgłoszenie serwisowe
     *
     *  1. Użytkownik jest zalogowany na koncie z rolą "Employee".
     *  2. Wyświetla listę torów.
     *  3. Naciska przycisk "Menu" i otwiera menu kontekstowe dla danego toru.
     *  4. Klika w "Dodaj zgłoszenie serwisowe".
     *
     * @param alleyId Identyfikator toru, dla którego dodać chcemy zgłoszenie.
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
     * Dodaje zgłsozenie serwisowe na bazie danych dostarczonych z widoku.
     *
     *  1. Użytkownik jest zalogowany na koncie z rolą "Employee".
     *  2. Wyświetla listę torów.
     *  3. Naciska przycisk "Menu" i otwiera menu kontekstowe dla danego toru.
     *  4. Klika w "Dodaj zgłoszenie serwisowe".
     *
     * @param serviceRequest Obiekt DTO z informacjami dotyczącymi zgłoszenia serwisowego.
     * @param alleyId Identyfikator toru.
     * @return Widok dodawania nowego zgłoszenia serwisowego dla toru.
     * @throws NotFoundException w przypadku, gdy tor nie zostanie znaleziony.
     * @return Widok zgłoszenia z informacją na temat statusu.
     */
    @POST
    @Path("new/{alleyId}")
    @RolesAllowed(MotRoles.ADD_SERVICE_REQUEST)
    @Produces(MediaType.TEXT_HTML)
    public String addServiceRequest(@BeanParam ServiceRequestDto serviceRequest, @PathParam("alleyId") Long alleyId) throws NotFoundException {
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
        } else {
            throw new NotFoundException();
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
     * @param id Identyfikator zgłoszenia serwisowego.
     * @return Widok z formularzem wypełnionym aktualnymi danymi.
     */
    @GET
    @Path("edit/{id}")
    @RolesAllowed(MotRoles.EDIT_SERVICE_REQUEST)
    @Produces(MediaType.TEXT_HTML)
    public String editServiceRequest(
            @QueryParam("idCache") Long idCache,
            @PathParam("id") Long id) {
        redirectUtil.injectFormDataToModels(idCache, models);

        ServiceRequestViewDto serviceRequest = null;
        try {
            serviceRequest = this.serviceRequestService.getById(id);
        } catch (Exception e) {
            displayError(localization.get("serviceRequestRetrievalError"));
        }

        if (serviceRequest != null) {
            ServiceRequestEditDto dto = Mapper.map(serviceRequest, ServiceRequestEditDto.class);
            this.models.put("sr", dto);
            this.models.put("alleyNumber", serviceRequest.getAlleyNumber());
        } else {
            throw new NotFoundException();
        }

        return EDIT_SERVICE_REQUEST_PAGE;
    }

    /**
     * Aktualizuje zgłoszenie serwisowe o podanym id w URL.
     *
     * @param serviceRequest Obiekt zawierający informacje o zaktualizowanym zgłoszeniu serwisowym.
     * @param id Identyfikator dla edytowanego zgłoszenia.
     * @throws NotFoundException W przypadku gdy nie podane id jest puste.
     * @return Widok edycji zgłoszenia z informację na temat statusu.
     */
    @POST
    @Path("edit/{id}")
    @RolesAllowed(MotRoles.EDIT_SERVICE_REQUEST)
    @Produces(MediaType.TEXT_HTML)
    public String editServiceRequest(
            @BeanParam ServiceRequestEditDto serviceRequest,
            @PathParam("id") Long id) throws NotFoundException {
        List<String> validateResult = dtoValidator.validate(serviceRequest);
        if (validateResult != null && !validateResult.isEmpty()) {
            return redirectUtil.redirect(
                    String.format(SERVICE_REQUEST_EDIT_ENDPOINT_PATTERN, id),
                    new FormData(serviceRequest, validateResult,
                            Collections.emptyList())
            );
        }
        if (id != null) {
            try {
                serviceRequest.setId(id);
                serviceRequestService.updateServiceRequest(serviceRequest);
            } catch (SsbdApplicationException e) {
                return redirectUtil.redirect(
                        String.format(SERVICE_REQUEST_EDIT_ENDPOINT_PATTERN, id),
                        new FormData(serviceRequest, Collections.singletonList(localization.get(e.getCode())),
                                Collections.emptyList())
                );
            }
        } else {
            throw new NotFoundException();
        }

        return redirectUtil.redirect(
                String.format(SERVICE_REQUEST_EDIT_ENDPOINT_PATTERN, id),
                new FormData(
                        null,
                        Collections.emptyList(),
                        Collections.singletonList(localization.get("serviceRequestEditSuccess"))
                )
        );
    }

    /**
     * Pobiera zgłoszenia serwisowe oraz wyświetla je użytkownikowi. W przypadku braku, zostanie zwrócona pusta lista.
     *
     *  1. Użytkownik jest zalogowany na koncie z rolą "Employee".
     *  2. Wyświetla listę zgłoszeń serwisowych.
     *  3. Użytkownik klika przycisk klika opcję w menu dla pracownika, "Zgłoszenia serwisowe".
     *  4. Wyświetlona zostaje lista ze zgłoszeniami.
     *
     * @return Widok ze zgłoszeniami serwisowymi
     */
    @GET
    @RolesAllowed(MotRoles.GET_SERVICE_REQUESTS)
    @Produces(MediaType.TEXT_HTML)
    public String getServiceRequests() {
        List<ServiceRequestViewDto> serviceRequests = new ArrayList<>();
        try {
            serviceRequests = this.serviceRequestService.getAllServiceRequests();
        } catch (SsbdApplicationException e) {
            displayError(localization.get("serviceRequestRetrievalError") + ". " + localization.get(e.getCode()));
        }
        models.put("srs", serviceRequests);
        return ALLEYS_LIST_VIEW;
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
