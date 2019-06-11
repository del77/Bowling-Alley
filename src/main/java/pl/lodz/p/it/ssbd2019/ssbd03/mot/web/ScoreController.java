package pl.lodz.p.it.ssbd2019.ssbd03.mot.web;

import pl.lodz.p.it.ssbd2019.ssbd03.SsbdApplication;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.Alley;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.mok.service.ResetPasswordService;
import pl.lodz.p.it.ssbd2019.ssbd03.mok.web.dto.EmailDto;
import pl.lodz.p.it.ssbd2019.ssbd03.mok.web.dto.validators.DtoValidator;
import pl.lodz.p.it.ssbd2019.ssbd03.mok.web.dto.validators.PasswordDtoValidator;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.service.ScoreService;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.web.dto.ItemDto;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.web.dto.ScoreDto;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.localization.LocalizedMessageProvider;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.redirect.FormData;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.redirect.RedirectUtil;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MotRoles;

import javax.annotation.security.PermitAll;
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
@Path("scores")
public class ScoreController implements Serializable {

    private static final String ADD_SCORE_VIEW = "mot/score/new.hbs";
    private static final String ADD_SCORE_URL = "/scores/new";

    @EJB
    private ScoreService scoreService;

    @Inject
    private Models models;

    @Inject
    private DtoValidator validator;

    @Inject
    private RedirectUtil redirectUtil;

    @Inject
    private LocalizedMessageProvider localization;

    /**
     * @return Formularz dodawania nowego wyniku
     */
    @GET
    @Path("/new/{reservation_id}")
    @RolesAllowed(MotRoles.ADD_SCORE)
    @Produces(MediaType.TEXT_HTML)
    public String addScore(@QueryParam("idCache") Long idCache) {
        redirectUtil.injectFormDataToModels(idCache, models);
        return ADD_SCORE_VIEW;
    }

    /**
     * Dodaje nowy wynik
     *
     * @param score obiekt zawierający informacje o wyniku
     * @return rezultat operacji
     */
    @POST
    @Path("/new/{reservation_id}")
    @RolesAllowed(MotRoles.ADD_SCORE)
    @Produces(MediaType.TEXT_HTML)
    public String addScore(@PathParam("reservation_id") Long reservation_id, @BeanParam ScoreDto score) {
        List<String> errorMessages = validator.validate(score);

        if (!errorMessages.isEmpty()) {
            return redirectUtil.redirectError(ADD_SCORE_URL + "/" + reservation_id, null, errorMessages);
        }

        try {
            scoreService.addNewScore(reservation_id, score);
        } catch (SsbdApplicationException e) {
            return redirectUtil.redirectError(ADD_SCORE_URL + "/" + reservation_id, null, Collections.singletonList(localization.get(e.getCode())));
        } catch (Exception e) {
            return redirectUtil.redirectError(ADD_SCORE_URL + "/" + reservation_id, null, Collections.singletonList(e.getLocalizedMessage()));
        }

        FormData formData = new FormData();
        formData.setInfos(Collections.singletonList(localization.get("addScoreSuccess")));
        return redirectUtil.redirect(ADD_SCORE_URL + "/" + reservation_id, formData);
    }
}
