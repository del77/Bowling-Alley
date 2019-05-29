package pl.lodz.p.it.ssbd2019.ssbd03.mor.web;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Comment;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.Reservation;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MorRoles;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.SessionScoped;
import javax.mvc.Controller;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.Serializable;

@SessionScoped
@Controller
public class ReservationController implements Serializable {
    /**
     * Pobiera widok pozwalający klientowi przejrzeć własne rezerwacje
     * @return Widok z formularzem.
     */
    @GET
    @RolesAllowed(MorRoles.GET_OWN_RESERVATIONS)
    @Produces(MediaType.TEXT_HTML)
    public String getOwnReservations() {
        throw new UnsupportedOperationException();
    }


    /**
     * Pobiera widok pozwalający klientowi dodać rezerwację
     * @return Widok z formularzem.
     */
    @GET
    @Path("myreservations/new")
    @RolesAllowed(MorRoles.CREATE_RESERVATION)
    @Produces(MediaType.TEXT_HTML)
    public String createReservation() {
        throw new UnsupportedOperationException();
    }

    /**
     * Dodaje nową rezerwację
     */
    @POST
    @Path("myreservations/new")
    @RolesAllowed(MorRoles.CREATE_RESERVATION)
    @Produces(MediaType.TEXT_HTML)
    public String createReservation(@BeanParam Reservation reservation) {
        throw new UnsupportedOperationException();
    }

    /**
     * Pobiera widok pozwalający klientowi edytować własną rezerwację
     * @return Widok z formularzem.
     */
    @GET
    @Path("myreservations/{id}/edit")
    @RolesAllowed(MorRoles.EDIT_OWN_RESERVATION)
    @Produces(MediaType.TEXT_HTML)
    public String editReservation() {
        throw new UnsupportedOperationException();
    }

    /**
     * Dodaje nową rezerwację
     */
    @POST
    @Path("myreservations/{id}/edit")
    @RolesAllowed(MorRoles.EDIT_OWN_RESERVATION)
    @Produces(MediaType.TEXT_HTML)
    public String editReservation(@BeanParam Reservation reservation) {
        throw new UnsupportedOperationException();
    }


    /**
     * Pozwala klientowi anulować rezerwację
     * @param id identyfikator rezerwacji do anulowania
     * @return rezulat operacji
     */
    @POST
    @Path("myreservations")
    @RolesAllowed(MorRoles.CANCEL_OWN_RESERVATION)
    @Produces(MediaType.TEXT_HTML)
    public String cancelReservation(@BeanParam Long id) {
        throw new UnsupportedOperationException();
    }

    /**
     * Pobiera widok pozwalający klientowi przejrzeć szegóły własnej rezerwacji
     * @return Widok z rezultatem.
     */
    @GET
    @Path("myreservations/{id}/details")
    @RolesAllowed(MorRoles.GET_OWN_RESERVATION_DETAILS)
    @Produces(MediaType.TEXT_HTML)
    public String getOwnReservationDetails(@PathParam("id") Long id) {
        throw new UnsupportedOperationException();
    }

    /**
     * Widok pozwalający klientowi dodać komentarz do rezerwacji
     * @param id wybrana rezerwacja
     * @return Widok z formularzem.
     */
    @GET
    @Path("myreservations/{id}/add-comment")
    @RolesAllowed(MorRoles.ADD_COMMENT_FOR_RESERVATION)
    @Produces(MediaType.TEXT_HTML)
    public String addCommentForReservation(@PathParam("id") Long id) {
        throw new UnsupportedOperationException();
    }

    /**
     * Dodaje komentarz do rezerwacji
     * @param id wybrana rezerwacja
     * @param comment komentarz do dodania
     * @return Widok z rezultatem.
     */
    @POST
    @Path("myreservations/{id}/add-comment")
    @RolesAllowed(MorRoles.ADD_COMMENT_FOR_RESERVATION)
    @Produces(MediaType.TEXT_HTML)
    public String addCommentForReservation(@BeanParam Long id, Comment comment) {
        throw new UnsupportedOperationException();
    }

    /**
     * Widok pozwalający klientowi edytowac własny komentarz do rezerwacji
     * @param id wybrany komentarz
     * @return Widok z formularzem.
     */
    @GET
    @Path("myreservations/{id}/edit-comment")
    @RolesAllowed(MorRoles.EDIT_COMMENT_FOR_OWN_RESERVATION)
    @Produces(MediaType.TEXT_HTML)
    public String editCommentForOwnReservation(@PathParam("id") Long id) {
        throw new UnsupportedOperationException();
    }

    /**
     * Dodaje komentarz do rezerwacji
     * @param id wybrana rezerwacja
     * @param comment komentarz do dodania
     * @return Widok z rezultatem.
     */
    @POST
    @Path("myreservations/{id}/edit-comment")
    @RolesAllowed(MorRoles.EDIT_COMMENT_FOR_OWN_RESERVATION)
    @Produces(MediaType.TEXT_HTML)
    public String editCommentForOwnReservation(@BeanParam Long id, Comment comment) {
        throw new UnsupportedOperationException();
    }


}
