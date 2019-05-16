package pl.lodz.p.it.ssbd2019.ssbd03.mor.web;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Reservation;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MorRoles;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.mvc.Controller;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Controller
@Path("reservations")
public class EmployeeReservationController {
    /**
     * Pobiera widok pozwalający pracownikowi dodać rezerwację
     * @return Widok z formularzem.
     */
    @GET
    @Path("new")
    @RolesAllowed(MorRoles.CREATE_RESERVATION_FOR_USER)
    @Produces(MediaType.TEXT_HTML)
    public String createReservation() {
        throw new UnsupportedOperationException();
    }

    /**
     * Dodaje nową rezerwację
     * @param reservation Dodawana rezerwacja
     * @return rezultat operacji
     */
    @POST
    @Path("new")
    @RolesAllowed(MorRoles.CREATE_RESERVATION_FOR_USER)
    @Produces(MediaType.TEXT_HTML)
    public String createReservation(Reservation reservation) {
        throw new UnsupportedOperationException();
    }

    /**
     * Pobiera widok pozwalający pracownikowi edytować własną rezerwację
     * @param id identyfikator edytowanej rezerwacji
     * @return Widok z formularzem.
     */
    @GET
    @Path("{id}/edit")
    @RolesAllowed(MorRoles.EDIT_RESERVATION_FOR_USER)
    @Produces(MediaType.TEXT_HTML)
    public String editReservation(@PathParam("id") Long id) {
        throw new UnsupportedOperationException();
    }

    /**
     * Pozwala pracownikowi anulować rezerwację
     * @param id identyfikator rezerwacji do anulowania
     * @return rezulat operacji
     */
    @POST
    @RolesAllowed(MorRoles.CANCEL_RESERVATION_FOR_USER)
    @Produces(MediaType.TEXT_HTML)
    public String cancelReservation(Long id) {
        throw new UnsupportedOperationException();
    }

    /**
     * Pobiera rezerwacje wybranego klienta
     * @param id identyfikator klienta
     * @return Widok z rezultatem.
     */
    @GET
    @Path("user/{id}")
    @RolesAllowed(MorRoles.GET_RESERVATIONS_FOR_USER)
    @Produces(MediaType.TEXT_HTML)
    public String getReservationsForUser(@PathParam("id") Long id) {
        throw new UnsupportedOperationException();
    }

    /**
     * Pobiera rezerwacje wybranego toru
     * @param id identyfikator toru
     * @return Widok z rezultatem.
     */
    @GET
    @Path("alleys/{id}")
    @RolesAllowed(MorRoles.GET_RESERVATIONS_FOR_ALLEY)
    @Produces(MediaType.TEXT_HTML)
    public String getReservationsForAlley(@PathParam("id") Long id) {
        throw new UnsupportedOperationException();
    }

    /**
     * Pobiera widok pozwalający pracownikowi przejrzeć szegóły wybranej rezererwacji
     * @param id identyfikator rezerwacji
     * @return Widok z rezultatem.
     */
    @GET
    @Path("reservations/{id}/details")
    @RolesAllowed(MorRoles.GET_OWN_RESERVATION_DETAILS)
    @Produces(MediaType.TEXT_HTML)
    public String getOwnReservationDetails(@PathParam("id") Long id) {
        throw new UnsupportedOperationException();
    }


    /**
     * Aktualizuje rezerwację
     * @param reservation obiekt zaktualizowanej rezerwacji
     * @return rezultat operacji
     */
    @POST
    @Path("{id}/edit")
    @RolesAllowed(MorRoles.EDIT_RESERVATION_FOR_USER)
    @Produces(MediaType.TEXT_HTML)
    public String editReservation(Reservation reservation) {
        throw new UnsupportedOperationException();
    }

    /**
     * Blokuje wybrany komentarz do rezerwacji
     * @param id wybrana komentarza
     * @return Widok z rezultatem.
     */
    @POST
    @Path("{id}/details")
    @RolesAllowed(MorRoles.DISABLE_COMMENT)
    @Produces(MediaType.TEXT_HTML)
    public String editCommentForReservation(Long id) {
        throw new UnsupportedOperationException();
    }
}
