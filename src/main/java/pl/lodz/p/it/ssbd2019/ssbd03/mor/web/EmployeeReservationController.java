package pl.lodz.p.it.ssbd2019.ssbd03.mor.web;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Reservation;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.service.ReservationService;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.ReservationFullDto;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.localization.LocalizedMessageProvider;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.redirect.FormData;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.redirect.RedirectUtil;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SessionScoped
@Controller
@Path("reservations")
public class EmployeeReservationController implements Serializable {

    private static final String ERROR = "errors";
    private static final String RESERVATION_LIST_VIEW = "mor/reservationList.hbs";
    private static final String RESERVATION_VIEW = "mor/reservation.hbs";
    private static final String RESERVATION_DETAILS_PATH = "/reservations/details/";

    @Inject
    private Models models;

    @EJB(beanName = "MORReservationService")
    private ReservationService reservationService;

    @Inject
    private LocalizedMessageProvider localization;

    @Inject
    private RedirectUtil redirectUtil;


    /**
     * Pobiera widok pozwalający pracownikowi dodać rezerwację
     *
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
     *
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
     *
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
     *
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
     *
     * @param id identyfikator klienta
     * @return Widok z rezultatem.
     */
    @GET
    @Path("user/{id}")
    @RolesAllowed(MorRoles.GET_RESERVATIONS_FOR_USER)
    @Produces(MediaType.TEXT_HTML)
    public String getReservationsForUser(@PathParam("id") Long id) {
        List<ReservationFullDto> reservations = new ArrayList<>();
        try {
            reservations = reservationService.getReservationsForUser(id);
        } catch (SsbdApplicationException e) {
            displayError(localization.get("reservationListError"));
        }
        models.put("reservationsList", reservations);
        models.put("reservationListHeading", localization.get("userReservationList"));
        return RESERVATION_LIST_VIEW;
    }

    /**
     * Pobiera rezerwacje wybranego toru
     *
     * @param id identyfikator toru
     * @return Widok z rezultatem.
     */
    @GET
    @Path("alley/{id}")
    @RolesAllowed(MorRoles.GET_RESERVATIONS_FOR_ALLEY)
    @Produces(MediaType.TEXT_HTML)
    public String getReservationsForAlley(@PathParam("id") Long id) {
        List<ReservationFullDto> reservations = new ArrayList<>();

        try {
            reservations = reservationService.getReservationsForAlley(id);
        } catch (SsbdApplicationException e) {
            displayError(localization.get("reservationListError"));
        }

        models.put("reservationsList", reservations);
        models.put("reservationListHeading", localization.get("alleyReservationList"));

        return RESERVATION_LIST_VIEW;
    }

    /**
     * Pobiera widok pozwalający pracownikowi przejrzeć szegóły wybranej rezererwacji
     *
     * @param id identyfikator rezerwacji
     * @return Widok z rezultatem.
     */
    @GET
    @Path("details/{id}")
    @RolesAllowed(MorRoles.GET_RESERVATION_DETAILS)
    @Produces(MediaType.TEXT_HTML)
    public String getReservationDetails(@PathParam("id") Long id, @QueryParam("idCache") Long idCache) {
        redirectUtil.injectFormDataToModels(idCache, models);
        try {
            ReservationFullDto reservation = reservationService.getReservationById(id);
            models.put("reservation", reservation);
        } catch (SsbdApplicationException e) {
            displayError(localization.get(e.getCode()));
        }

        return RESERVATION_VIEW;
    }


    /**
     * Aktualizuje rezerwację
     *
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
     * edytuje wybrany komentarz do rezerwacji
     *
     * @param id wybrana komentarza
     * @return Widok z rezultatem.
     */
    @POST
    @Path("{id}/details/edit")
    @RolesAllowed(MorRoles.EDIT_COMMENT_FOR_RESERVATION)
    @Produces(MediaType.TEXT_HTML)
    public String editCommentForReservation(Long id) {
        throw new UnsupportedOperationException();
    }

    /**
     * Blokuje wybrany komentarz do rezerwacji
     *
     * @param id wybrana komentarza
     * @return Widok z rezultatem.
     */
    @POST
    @Path("details/{reservationId}/disable-comment/{id}")
    @RolesAllowed(MorRoles.DISABLE_COMMENT)
    @Produces(MediaType.TEXT_HTML)
    public String disableComment(@PathParam("reservationId") Long reservationId, @PathParam("id") Long id) {
        try {
            reservationService.disableComment(id);
            FormData formData = new FormData();
            String message = localization.get("commentDisabled");
            formData.setInfos(Collections.singletonList(message));
            return redirectUtil.redirect(RESERVATION_DETAILS_PATH + reservationId, formData);
        } catch (SsbdApplicationException e) {
            return redirectUtil.redirectError(
                    RESERVATION_DETAILS_PATH + reservationId,
                    null,
                    Collections.singletonList(localization.get(e.getCode()))
            );
        }
    }

    private void displayError(String s) {
        models.put(ERROR, Collections.singletonList(s));
    }

}
