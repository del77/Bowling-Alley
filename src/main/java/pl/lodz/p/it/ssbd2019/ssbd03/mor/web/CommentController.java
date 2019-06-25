package pl.lodz.p.it.ssbd2019.ssbd03.mor.web;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.repository.ReservationRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.service.CommentService;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.CommentDto;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.DtoValidator;
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
import java.util.Collections;
import java.util.List;

@SessionScoped
@Controller
@Path("comments")
public class CommentController implements Serializable {

    private static final String ERROR = "errors";
    private static final String ADD_COMMENT_VIEW = "mor/addCommentForm.hbs";
    private static final String ADD_COMMENT_PATH = "/comments/add/";

    @EJB(beanName = "MORCommentService")
    private CommentService commentService;

    @EJB(beanName = "MORReservationRepository")
    private ReservationRepositoryLocal reservationRepository;

    @Inject
    private Models models;

    @Inject
    private LocalizedMessageProvider localization;

    @Inject
    private RedirectUtil redirectUtil;

    @Inject
    private DtoValidator validator;

    /**
     * Widok pozwalający klientowi dodać komentarz do rezerwacji
     *
     * 1. Użytkownik jest zalogowany na koncie z rolą "CLIENT"
     * 2. Użytkownik klika przycisk "Dodaj komentarz" w szczegółach zakończonej rezerwacji.
     * 3. System wyświetla formularz dodawania komentarza.
     * 4. Użytkownik podaje treść komentarza i zatwierdza.
     * 5. System zapisuje zmiany i wyświetla komunikat o dodaniu komentarza.
     *
     * @param reservationId wybrana rezerwacja
     * @return Widok z formularzem.
     */
    @GET
    @Path("add/{id}")
    @RolesAllowed(MorRoles.ADD_COMMENT_FOR_RESERVATION)
    @Produces(MediaType.TEXT_HTML)
    public String addCommentForReservation(@PathParam("id") Long reservationId, @QueryParam("idCache") Long idCache) {
        redirectUtil.injectFormDataToModels(idCache, models);
        boolean reservationExists = false;
        try {
            reservationExists = reservationRepository.existsById(reservationId);
        } catch (SsbdApplicationException e) {
            displayError(e.getCode());
        }
        if (!reservationExists) {
            throw new NotFoundException();
        }
        return ADD_COMMENT_VIEW;
    }

    /**
     * Dodaje komentarz do rezerwacji
     *
     * @param reservationId wybrana rezerwacja
     * @param comment       komentarz do dodania
     * @return Widok z rezultatem.
     */
    @POST
    @Path("add/{id}")
    @RolesAllowed(MorRoles.ADD_COMMENT_FOR_RESERVATION)
    @Produces(MediaType.TEXT_HTML)
    public String addCommentForReservation(@PathParam("id") Long reservationId, @BeanParam CommentDto comment) throws
            NotFoundException {

        List<String> validateResult = validator.validate(comment);
        if (validateResult != null && !validateResult.isEmpty()) {
            return redirectUtil.redirect(
                    ADD_COMMENT_PATH + reservationId,
                    new FormData(comment, validateResult,
                            Collections.emptyList())
            );
        }

        try {
            commentService.addComment(reservationId, comment);
        } catch (SsbdApplicationException e) {
            return redirectUtil.redirect(ADD_COMMENT_PATH + reservationId,
                    new FormData(
                            comment,
                            Collections.singletonList(localization.get("addCommentFailure") + "\n"
                                    + localization.get(e.getCode())),
                            Collections.emptyList()));
        }
        FormData formData = new FormData();
        formData.setInfos(Collections.singletonList(localization.get("addCommentSuccess")));
        return redirectUtil.redirect(ADD_COMMENT_PATH + reservationId, formData);
    }

    private void displayError(String s) {
        models.put(ERROR, Collections.singletonList(s));
    }
}
