package pl.lodz.p.it.ssbd2019.ssbd03.mot.web;


import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.service.AlleyService;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.service.ScoreService;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.web.dto.AlleyCreationDto;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.web.dto.ScoreDto;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.web.dto.AlleyLockDto;
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

    @EJB(beanName = "MOTScoreService")
    private ScoreService scoreService;

    private static final String BASE_PATH = "alleys";
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
     * 1. Użytkownik jest zalogowany na koncie z rolą "Employee"
     * 2. Użytkownik klika przycisk "dodaj tor"
     * 3. System pyta o numer nowego toru
     * 4. Użytkownik podaje numer i zatwierdza
     * 5. System tworzy obiekt toru z domyślną wartością "active: true"
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
     * 1. Użytkownik jest zalogowany na koncie z rolą "Employee".
     * 2. Użytkownik klika przycisk "Wyświetl wszystkie tory".
     * 3. System wyświetla listę torów posortowanych rosnąco po numerach.
     * (zablokuj/wyłącz) 4. Użytkownik klika przycisk "Zablokuj/Wyłącz tor" obok toru, który go interesuje.
     * (zablokuj/wyłącz) 5. System zmienia pole "active" odpowiedniego obiektu reprezentującego tor o podanym numerze na "false"
     * (odblokuj) 4. Użytkownik klika przycisk "Odblokuj tor" obok toru, który go interesuje.
     * (odblokuj) 5. System zmienia pole "active" odpowiedniego obiektu reprezentującego tor o podanym numerze na "true"
     *
     * @param dto     dto z id toru, któremu należy zmienić flagę aktywności
     * @param idCache opcjonalny, identyfikator cache z danymi po przekierowaniu
     * @return Widok z listą torów oraz komunikatem o powodzeniu lub błędzie
     */
    @POST
    @RolesAllowed(MotRoles.ENABLE_DISABLE_ALLEY)
    @Produces(MediaType.TEXT_HTML)
    public String updateLockStatusOnAlley(@BeanParam AlleyLockDto dto, @QueryParam("idCache") Long idCache) {
        boolean active = dto.getActive() != null; // workaround - checkbox returns null when unchecked
        try {
            alleyService.updateLockStatusOnAlleyById(dto.getId(), active);
            FormData formData = new FormData();
            String message = active ? localization.get("unlockedAlleySuccess") : localization.get("lockedAlleySuccess");
            formData.setInfos(Collections.singletonList(message));
            return redirectUtil.redirect(BASE_PATH, formData);
        } catch (SsbdApplicationException e) {
            return redirectUtil.redirectError(
                    BASE_PATH,
                    null,
                    Collections.singletonList(localization.get(e.getCode()) + "\n" + localization.get("couldntLockAlley"))
            );
        }
    }

    /**
     * 1. Użytkownik jest zalogowany na koncie z rolą "Employee" lub "Client".
     * 2. Użytkownik klika przycisk "Wyświetl wszystkie tory".
     * 3. System wyświetla listę torów posortowanych rosnąco po numerach.
     * 4. Użytkownik klika przycisk "Pokaż historię rozgrywek" obok toru, który go interesuje.
     * 5. System wyświetla listę rozgrywek na danym torze.
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
     * Scenariusz:
     *
     * 1) Użytkownik jest zalogowany na koncie z rolą "Employee" lub "Client".
     * 2) Użytkownik klika przycisk "Wyświetl listę torów".
     * 3) System wyświetla listę torów posortowanych rosnąco po numerach.
     *
     * @param id identyfikator cache
     * @return Widok ze wszystkimi torami
     */
    @GET
    @RolesAllowed(MotRoles.GET_ALLEYS_LIST)
    @Produces(MediaType.TEXT_HTML)
    public String getAllAlleys(@QueryParam("idCache") Long id) {
        redirectUtil.injectFormDataToModels(id, models);
        try {
            models.put("alleys", alleyService.getAllAlleys());
        } catch (SsbdApplicationException e) {
            displayError(localization.get(e.getCode()));
        }
        return ALLEY_LIST_VIEW;
    }


    private void displayError(String s) {
        models.put(ERROR, Collections.singletonList(s));
    }
}
