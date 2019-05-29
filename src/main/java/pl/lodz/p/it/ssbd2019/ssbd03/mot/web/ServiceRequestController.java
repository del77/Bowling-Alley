package pl.lodz.p.it.ssbd2019.ssbd03.mot.web;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.ServiceRequest;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MotRoles;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.mvc.Controller;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.Serializable;

@SessionScoped
@Controller
@Path("service-requests")
public class ServiceRequestController implements Serializable {
    /**
     * Zwraca widok pozwalający dodać nowe zgłoszenie serwisowe
     * @return Widok z formularzem pozwalającym dodać zgłoszenie-
     */
    @GET
    @Path("new")
    @RolesAllowed(MotRoles.ADD_SERVICE_REQUEST)
    @Produces(MediaType.TEXT_HTML)
    public String addServiceRequest() {
        throw new UnsupportedOperationException();
    }

    /**
     * Dodaje nowe zgłoszenie serwisowe
     * @param serviceRequest obiekt zawierający informacje o zgłoszeniu serwisowym
     */
    @POST
    @Path("new")
    @RolesAllowed(MotRoles.ADD_SERVICE_REQUEST)
    @Produces(MediaType.TEXT_HTML)
    public String addServiceRequest(@BeanParam ServiceRequest serviceRequest) {
        throw new UnsupportedOperationException();
    }

    /**
     * Zwraca widok pozwalający edytować zgłoszenie serwisowe
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
     * @return Widok ze zgłoszeniami serwisowymi
     */
    @GET
    @RolesAllowed(MotRoles.GET_SERVICE_REQUESTS)
    @Produces(MediaType.TEXT_HTML)
    public String getServiceRequests() {
        throw new UnsupportedOperationException();
    }
}
