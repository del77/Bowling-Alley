package pl.lodz.p.it.ssbd2019.ssbd03.mot.web;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.service.ScoreService;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.web.dto.AddScoreDto;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.web.dto.ScoreDto;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.DtoValidator;
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
import java.util.Collections;
import java.util.List;

@SessionScoped
@Controller
@Path("scores")
public class ScoreController implements Serializable {

    private static final String ADD_SCORE_VIEW = "mot/score/new.hbs";
    private static final String USER_SCORES_LIST_VIEW = "mot/score/scoreslist.hbs";

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
     * 1. Użytkownik jest zalogowany na koncie z rolą "Employee".
     * 2. System wyświetla listę rezerwacji.
     * 3. Użytkownik klika przycisk "Dodaj wynik rozgrywki" przy wybranej rezerwacji
     * 4. Użytkownik podaje wynik oraz login gracza
     * 5. System tworzy nowy obiekt reprezentujący wynik gracza podczas danej rezerwacji
     *
     * @param reservation_id ID rezerwacji
     * @param idCache        identyfikator zapamiętanego stanu formularza w cache
     * @return Formularz dodawania nowego wyniku
     */
    @GET
    @Path("/new/{reservation_id}")
    @RolesAllowed(MotRoles.ADD_SCORE)
    @Produces(MediaType.TEXT_HTML)
    public String addScore(@PathParam("reservation_id") Long reservation_id, @QueryParam("idCache") Long idCache) {
        redirectUtil.injectFormDataToModels(idCache, models);
        return ADD_SCORE_VIEW;
    }


    /**
     * Dodanie nowego wyniku
     *
     * @param reservation_id ID rezerwacji
     * @param score          DTO zawierające wynik
     * @return rezultat operacji
     */
    @POST
    @Path("/new/{reservation_id}")
    @RolesAllowed(MotRoles.ADD_SCORE)
    @Produces(MediaType.TEXT_HTML)
    public String addScore(@PathParam("reservation_id") Long reservation_id, @BeanParam AddScoreDto score) {
        List<String> errorMessages = validator.validate(score);

        if (!errorMessages.isEmpty()) {
            return redirectUtil.redirectError(ADD_SCORE_URL + "/" + reservation_id, null, errorMessages);
        }

        try {
            scoreService.addNewScore(reservation_id, score);
        } catch (SsbdApplicationException e) {
            return redirectUtil.redirectError(ADD_SCORE_URL + "/" + reservation_id, null, Collections.singletonList(localization.get(e.getCode())));
        }

        FormData formData = new FormData();
        formData.setInfos(Collections.singletonList(localization.get("addScoreSuccess")));
        return redirectUtil.redirect(ADD_SCORE_URL + "/" + reservation_id, formData);
    }

    /**
     * Pobiera historię wyników wybranego uzytkownika
     *
     * 1. Użytkownik jest zalogowany na koncie z rolą "Employee".
     * 2. Użytkownik klika przycisk "Wyświetl wszystkich użytkowników"
     * 3. System wyświetla listę użytkowników
     * 4. Użytkownik klika przycisk "Pokaż wyniki" obok Clienta, którego historię chce wyświetlić
     * 5. System wyświetla listę wyników posortowaną od najnowszego do najstarszego
     *
     * @param userId identyfikator użytkownika
     * @return widok z listą wyników użytkownika
     */
    @GET
    @RolesAllowed(MotRoles.SHOW_USER_SCORE_HISTORY)
    @Produces(MediaType.TEXT_HTML)
    @Path("user/{user_id}")
    public String showUserScores(@PathParam("user_id") Long userId) {
        try {
            List<ScoreDto> userScores = scoreService.getScoresForUser(userId);
            models.put("scoresList", userScores);
        } catch (SsbdApplicationException e) {
            displayError(localization.get(e.getCode()));
        }
        return USER_SCORES_LIST_VIEW;
    }

    /**
     * metoda pomocnicza przekazująca do widoku wiadomość o błędzie
     *
     * @param s wiadomość do wyświetlenia
     */
    private void displayError(String s) {
        models.put("errors", Collections.singletonList(s));
    }
}
