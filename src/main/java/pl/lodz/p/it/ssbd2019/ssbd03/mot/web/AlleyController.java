package pl.lodz.p.it.ssbd2019.ssbd03.mot.web;


import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.service.AlleyService;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.service.ReservationService;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.service.ScoreService;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.web.dto.AlleyCreationDto;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.web.dto.ReservationFullDto;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SessionScoped
@Controller
@Path("alleys")
public class AlleyController implements Serializable {

    @Inject
    protected Models models;

    @Inject
    protected LocalizedMessageProvider localization;

    @Inject
    private DtoValidator validator;

    @Inject
    protected RedirectUtil redirectUtil;

    @EJB(beanName = "MOTAlleyService")
    private AlleyService alleyService;

    @EJB(beanName = "MOTReservationService")
    private ReservationService reservationService;

    @EJB(beanName = "MOTScoreService")
    private ScoreService scoreService;

    private static final String NEW_ALLEY_URL = "alleys/new";
    private static final String ADD_ALLEY_VIEW_URL = "alleys/new/newAlley.hbs";

    private static final String ALLEY_HISTORY_VIEW = "mot/history/history.hbs";

    private List<String> errorMessages = new ArrayList<>();

    private static final String ERROR = "errors";
    private static final String ALLEY_LIST_VIEW = "mot/alleysList.hbs";

    /**
     * Pobiera widok dodawania toru.
     *
     * @return Widok umożliwiający dodanie toru.
     */
    @GET
    @Path("new")
    @RolesAllowed(MotRoles.ADD_ALLEY)
    @Produces(MediaType.TEXT_HTML)
    public String addAlley(@QueryParam("idCache") Long id) {
        redirectUtil.injectFormDataToModels(id, models);
        return ADD_ALLEY_VIEW_URL;
    }


    /**
     * Dodaje nowy tor
     *
     * @param alleyDto obiekt zawierający informacje o torze
     * @return rezultat operacji
     */
    @POST
    @Path("new")
    @RolesAllowed(MotRoles.ADD_ALLEY)
    @Produces(MediaType.TEXT_HTML)
    public String addAlley(@BeanParam AlleyCreationDto alleyDto) {
        models.put("data", alleyDto);
        errorMessages.clear();
        errorMessages.addAll(validator.validate(alleyDto));
        if (!errorMessages.isEmpty()) {
            return redirectUtil.redirectError("/alleys/new", alleyDto, errorMessages);
        }
        try {
            alleyService.addAlley(alleyDto);
        } catch (SsbdApplicationException e) {
            errorMessages.add(localization.get(e.getCode()));
        }
        if (!errorMessages.isEmpty()) {
            return redirectUtil.redirectError("/alleys/new", alleyDto, errorMessages);
        }

        FormData formData = FormData.builder()
                .data(null)
                .infos(Collections.singletonList(String.format(
                        "%s",
                        localization.get("alleySuccessfullyCreated"))
                ))
                .build();
        return redirectUtil.redirect(NEW_ALLEY_URL, formData);
    }

    /**
     * Wyświetla widok pozwalający odblokowac lub zablokowac tor
     *
     * @return Widok z listą torow oraz ich statusem zablokowania
     */
    @GET
    @RolesAllowed(MotRoles.ENABLE_DISABLE_ALLEY)
    @Path("state")
    @Produces(MediaType.TEXT_HTML)
    public String updateLockStatusOnAlley() {
        throw new UnsupportedOperationException();
    }

    /**
     * Zmienia status zablokowania toru z podanym identyfikatorem
     *
     * @return Widok z listą torow oraz komunikatem o powodzeniu lub błędzie
     */
    @POST
    @RolesAllowed(MotRoles.ENABLE_DISABLE_ALLEY)
    @Path("state")
    @Produces(MediaType.TEXT_HTML)
    public String updateLockStatusOnAlley(@BeanParam Long id, boolean state) {
        throw new UnsupportedOperationException();
    }

    /**
     * Wyświetla historię rozgrywek na torze.
     *
     * @return Widok z historią rozgrywek dla toru.
     */
    @GET
    @RolesAllowed(MotRoles.GET_ALLEY_GAMES_HISTORY)
    @Path("history/{id}")
    @Produces(MediaType.TEXT_HTML)
    public String showGamesHistoryForAlley(@PathParam("id") Long id) {
        List<ScoreDto> scoreDtos = new ArrayList<>();
        try {
            scoreDtos.addAll(scoreService.getScoresForAlley(id));
        } catch (SsbdApplicationException e) {
            displayError(localization.get(e.getCode()));
        }
        models.put("scores", scoreDtos);
        return ALLEY_HISTORY_VIEW;

    }

    /**
     * Pobiera wszystkie tory
     *
     * @return Widok ze wszystkimi torami
     */
    @GET
    @RolesAllowed(MotRoles.GET_ALLEYS_LIST)
    @Produces(MediaType.TEXT_HTML)
    public String getAllAlleys() {
        try {
            models.put("alleys", alleyService.getAllAlleys());
        } catch (SsbdApplicationException e) {
            displayError(localization.get("alleysListError"));
        }
        return ALLEY_LIST_VIEW;
    }


    private void displayError(String s) {
        models.put(ERROR, Collections.singletonList(s));
    }
}
