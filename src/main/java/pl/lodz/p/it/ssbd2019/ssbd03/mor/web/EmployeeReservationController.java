package pl.lodz.p.it.ssbd2019.ssbd03.mor.web;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Reservation;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.service.ReservationService;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.ReservationDto;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.TimeRangeDto;
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
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SessionScoped
@Controller
@Path("reservations")
public class EmployeeReservationController implements Serializable {

    private static final String ERROR = "errors";
    private static final String RESERVATION_LIST_VIEW = "mor/reservationList.hbs";
    private static final String AVAILABLE_ALLEYS_VIEW = "mor/availableAlleysList.hbs";

    @Inject
    private Models models;

    @EJB
    private ReservationService reservationService;

    @Inject
    private LocalizedMessageProvider localization;


    @GET
    @Path("available-alleys")
    @RolesAllowed(MorRoles.CREATE_RESERVATION_FOR_USER)
    @Produces(MediaType.TEXT_HTML)
    public String createReservation() {
        models.put("startDate", Timestamp.from(Instant.now()));
        models.put("numberOfHours", 1);
        return AVAILABLE_ALLEYS_VIEW;
    }


    @POST
    @Path("available-alleys")
    @RolesAllowed(MorRoles.CREATE_RESERVATION_FOR_USER)
    @Produces(MediaType.TEXT_HTML)
    public String createReservation(TimeRangeDto timeRangeDto) {
        models.put("startDate", timeRangeDto.getStartDate());
        models.put("numberOfHours", timeRangeDto.getNumberOfHours() + 3);
        return AVAILABLE_ALLEYS_VIEW;
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
        List<ReservationDto> reservations = new ArrayList<>();
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
        List<ReservationDto> reservations = new ArrayList<>();

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
    @Path("reservations/{id}/details")
    @RolesAllowed(MorRoles.GET_RESERVATION_DETAILS)
    @Produces(MediaType.TEXT_HTML)
    public String getReservationDetails(@PathParam("id") Long id) {
        throw new UnsupportedOperationException();
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
    @Path("{id}/disable")
    @RolesAllowed(MorRoles.DISABLE_COMMENT)
    @Produces(MediaType.TEXT_HTML)
    public String disableComment(Long id) {
        throw new UnsupportedOperationException();
    }

    private void displayError(String s) {
        models.put(ERROR, Collections.singletonList(s));
    }

}
