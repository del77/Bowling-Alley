package pl.lodz.p.it.ssbd2019.ssbd03.mor.web;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Comment;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.Reservation;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.service.ReservationService;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.AvailableAlleyDto;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.NewReservationAllForm;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.NewReservationDto;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.ReservationFullDto;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.DtoValidator;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.helpers.ReservationValidator;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SessionScoped
@Controller
@Path("myreservations")
public class ReservationController implements Serializable {

    private static final String ERROR = "errors";
    private static final String RESERVATION_LIST_VIEW = "mor/reservationList.hbs";
    private static final String RESERVATION_VIEW = "mor/reservation.hbs";
    private static final String NEW_RESERVATION_VIEW = "mor/newReservation.hbs";
    private static final String NEW_RESERVATION_URL = "/myreservations/new";
    private static final String RESERVATION_DETAILS_PATH = "/myreservations/details/";

    @EJB(beanName = "MORReservationService")
    private ReservationService reservationService;

    @Inject
    private Models models;

    @Inject
    private LocalizedMessageProvider localization;

    @Inject
    private RedirectUtil redirectUtil;

    @Inject
    private DtoValidator validator;

    private transient NewReservationDto newReservationDto;

    /**
     * Pobiera widok pozwalający klientowi przejrzeć własne rezerwacje
     *
     * 1. Użytkownik jest zalogowany na koncie z rolą "CLIENT"
     * 2. Użytkownik przechodzi na listę "Moje rezerwacje"
     * 3. System wyświetla listę rezerwacji przypisanych do konta zalogowanego użytkownika, lista może być pusta.
     *
     * @return Widok z listą rezerwacji.
     */
    @GET
    @RolesAllowed(MorRoles.GET_OWN_RESERVATIONS)
    @Produces(MediaType.TEXT_HTML)
    public String getOwnReservations() {
        try {
            String login = (String) models.get("userName");
            List<ReservationFullDto> reservations = reservationService.getReservationsByUserLogin(login);
            models.put("reservationsList", reservations);
            models.put("reservationListHeading", localization.get("ownReservationList"));
            models.put("reservationContext", "myreservations");
        } catch (SsbdApplicationException e) {
            displayError(localization.get("reservationListError"));
        }
        return RESERVATION_LIST_VIEW;
    }

    /**
     * Pobiera widok pozwalający klientowi stworzyć rezerwację.
     *
     * @return Widok z formularzem.
     */
    @GET
    @Path("new")
    @RolesAllowed(MorRoles.CREATE_RESERVATION)
    @Produces(MediaType.TEXT_HTML)
    public String getAvailableAlleys(@QueryParam("idCache") Long idCache) {
        redirectUtil.injectFormDataToModels(idCache, models);
        return NEW_RESERVATION_VIEW;
    }

    /**
     * Pobiera dostępne tory w zadanym przedziale czasowym.
     *
     * @param newReservationDto dane rezerwacji
     * @return widok z dostępnymi torami
     */
    @POST
    @Path("new")
    @RolesAllowed(MorRoles.CREATE_RESERVATION)
    @Produces(MediaType.TEXT_HTML)
    public String getAvailableAlleys(@BeanParam NewReservationDto newReservationDto) {
        List<String> errorMessages = validator.validate(newReservationDto);

        NewReservationAllForm newReservationAllForm = new NewReservationAllForm();
        newReservationAllForm.setNewReservationDto(newReservationDto);

        if (!errorMessages.isEmpty()) {
            return redirectUtil.redirectError(NEW_RESERVATION_URL, newReservationAllForm, errorMessages);
        }

        try {
            List<AvailableAlleyDto> availableAlleys = reservationService.getAvailableAlleysInTimeRange(newReservationDto);
            this.newReservationDto = newReservationDto;

            newReservationAllForm.setAvailableAlleys(availableAlleys);
            FormData formData = FormData.builder().data(newReservationAllForm).build();
            return redirectUtil.redirect(NEW_RESERVATION_URL, formData);
        } catch (SsbdApplicationException e) {
            return redirectUtil.redirectError(NEW_RESERVATION_URL, newReservationAllForm, Arrays.asList(localization.get(e.getCode())));
        }
    }

    /**
     * Tworzy rezerwacje
     *
     * Scenariusz:
     *     1) Użytkownik jest zalogowany na koncie z rolą "Client".
     *     2) System wyświetla wybór godziny
     *     3) Użytkownik wybiera godzinę
     *     4) System wyświetla dostępne tory
     *     5) Użytkownik wybiera tor
     *     6) Użytkownik klika zatwierdź
     *     7) System przekierowuje na stronę rezerwacji
     *
     * @param alleyId
     * @return informacja o wyniku rezerwacji
     */
    @GET
    @Path("new/{alley_id}")
    @RolesAllowed(MorRoles.CREATE_RESERVATION)
    @Produces(MediaType.TEXT_HTML)
    public String createReservation(@PathParam("alley_id") Long alleyId) {
        if (newReservationDto == null) {
            return redirectUtil.redirect(NEW_RESERVATION_URL, new FormData());
        }

        List<String> errorMessages = validator.validate(newReservationDto);

        NewReservationAllForm newReservationAllForm = new NewReservationAllForm();
        newReservationAllForm.setNewReservationDto(newReservationDto);
        if (!errorMessages.isEmpty()) {
            return redirectUtil.redirectError(NEW_RESERVATION_URL, newReservationAllForm, errorMessages);
        }

        FormData formData = new FormData();
        formData.setData(newReservationAllForm);
        try {
            String login = (String) models.get("userName");
            reservationService.addReservation(newReservationDto, alleyId, login);
            formData.setInfos(Collections.singletonList(localization.get("newReservationCreated")));
            return redirectUtil.redirect(NEW_RESERVATION_URL, formData);
        } catch (SsbdApplicationException e) {
            formData.setErrors(Collections.singletonList(localization.get(e.getCode())));
            return redirectUtil.redirect(NEW_RESERVATION_URL, formData);
        }
    }


    /**
     * Pobiera widok pozwalający klientowi edytować własną rezerwację
     *
     * @return Widok z formularzem.
     */
    @GET
    @Path("{id}/edit")
    @RolesAllowed(MorRoles.EDIT_OWN_RESERVATION)
    @Produces(MediaType.TEXT_HTML)
    public String editReservation() {
        throw new UnsupportedOperationException();
    }

    /**
     * Dodaje nową rezerwację
     */
    @POST
    @Path("{id}/edit")
    @RolesAllowed(MorRoles.EDIT_OWN_RESERVATION)
    @Produces(MediaType.TEXT_HTML)
    public String editReservation(@BeanParam Reservation reservation) {
        throw new UnsupportedOperationException();
    }

    /**
     * Pobiera widok pozwalający klientowi przejrzeć szegóły własnej rezerwacji
     *
     * 1. Użytkownik jest zalogowany na koncie z rolą "Client"
     * 2. Użytkownik przechodzi na listę swoich rezerwacji
     * 3. Użytkownik klika "Pokaż szczegóły"
     * 4. System wyświetla szczegóły rezerwacji
     *
     * @param reservationId identyfikator rezerwacji
     * @param idCache       opcjonalny identyfikator do obsługi przekierowań
     * @return Widok z rezultatem.
     */
    @GET
    @Path("details/{id}")
    @RolesAllowed(MorRoles.GET_OWN_RESERVATION_DETAILS)
    @Produces(MediaType.TEXT_HTML)
    public String getOwnReservationDetails(@PathParam("id") Long reservationId,
                                           @QueryParam("idCache") Long idCache) {
        redirectUtil.injectFormDataToModels(idCache, models);
        String login = (String) models.get("userName");
        try {
            ReservationFullDto reservation = reservationService.getUserReservationById(reservationId, login);
            boolean isExpired = ReservationValidator.isExpired(reservation.getStartDate());
            Boolean isCancelable = !isExpired && reservation.isActive();
            models.put("reservation", reservation);
            models.put("isExpired", isExpired);
            models.put("isCancelable", isCancelable);
            models.put("ownReservation", true);
        } catch (SsbdApplicationException e) {
            displayError(localization.get(e.getCode()));
        }
        return RESERVATION_VIEW;
    }

    /**
     * Pozwala klientowi anulować własną rezerwację
     *
     * @param reservationId identyfikator rezerwacji
     * @return rezulat operacji
     */
    @POST
    @Path("details/{id}")
    @RolesAllowed(MorRoles.CANCEL_OWN_RESERVATION)
    @Produces(MediaType.TEXT_HTML)
    public String cancelReservation(@PathParam("id") Long reservationId) {
        try {
            reservationService.cancelReservation(reservationId);
            FormData formData = new FormData();
            String message = localization.get("reservationCancelSuccess");
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

    /**
     * Widok pozwalający klientowi dodać komentarz do rezerwacji
     *
     * @param id wybrana rezerwacja
     * @return Widok z formularzem.
     */
    @GET
    @Path("{id}/add-comment")
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
    @Path("{id}/add-comment")
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
    @Path("{id}/edit-comment")
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
    @Path("{id}/edit-comment")
    @RolesAllowed(MorRoles.EDIT_COMMENT_FOR_OWN_RESERVATION)
    @Produces(MediaType.TEXT_HTML)
    public String editCommentForOwnReservation(@BeanParam Long id, Comment comment) {
        throw new UnsupportedOperationException();
    }

    private void displayError(String s) {
        models.put(ERROR, Collections.singletonList(s));
    }
}
