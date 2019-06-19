package pl.lodz.p.it.ssbd2019.ssbd03.mor.web;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Comment;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.Reservation;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.service.ReservationService;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.ReservationFullDto;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.localization.LocalizedMessageProvider;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MorRoles;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.mvc.Controller;
import javax.mvc.Models;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.Serializable;
import java.util.Collections;

@SessionScoped
@Controller
@Path("myreservations")
public class ReservationController implements Serializable {

    private static final String ERROR = "errors";
    private static final String RESERVATION_VIEW = "mor/reservation.hbs";

    @EJB(name = "MORReservationService")
    private ReservationService reservationService;

    @Inject
    private Models models;

    @Inject
    private LocalizedMessageProvider localization;

    /**
     * Pobiera widok pozwalający klientowi przejrzeć własne rezerwacje
     *
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
     *
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
     *
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
     *
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
     *
     * @return Widok z rezultatem.
     */
    @GET
    @Path("details/{id}")
    @RolesAllowed(MorRoles.GET_OWN_RESERVATION_DETAILS)
    @Produces(MediaType.TEXT_HTML)
    public String getOwnReservationDetails(@PathParam("id") Long id) {
        String login = (String) models.get("userName");

        try {
            ReservationFullDto reservation = reservationService.getUserReservationById(id, login);
            models.put("reservation", reservation);
        } catch (SsbdApplicationException e) {
            displayError(localization.get(e.getCode()));
        }

        return RESERVATION_VIEW;
    }

    /**
     * Widok pozwalający klientowi dodać komentarz do rezerwacji
     *
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
     *
     * @param id      wybrana rezerwacja
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
     *
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
     *
     * @param id      wybrana rezerwacja
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

    private void displayError(String s) {
        models.put(ERROR, Collections.singletonList(s));
    }
}
