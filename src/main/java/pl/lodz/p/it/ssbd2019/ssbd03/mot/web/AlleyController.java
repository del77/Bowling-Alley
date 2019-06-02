package pl.lodz.p.it.ssbd2019.ssbd03.mot.web;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Alley;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.service.AlleyService;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.localization.LocalizedMessageProvider;
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

@SessionScoped
@Controller
@Path("alleys")
public class AlleyController implements Serializable {
    
    private static final String ERROR = "errors";
    private static final String ALLEY_LIST_VIEW = "alleys/alleysList.hbs";
    
    @Inject
    private Models models;
    
    @EJB
    private AlleyService alleyService;
    
    @Inject
    private LocalizedMessageProvider localization;
    
    /**
     * Pobiera widok dodawania toru.
     * @return Widok umożliwiający dodanie toru.
     */
    @GET
    @Path("new")
    @RolesAllowed(MotRoles.ADD_ALLEY)
    @Produces(MediaType.TEXT_HTML)
    public String addAlley() {
        throw new UnsupportedOperationException();
    }

    /**
     * Dodaje nowy tor
     * @param alley obiekt zawierający informacje o torze
     * @return rezultat operacji
     */
    @POST
    @Path("new")
    @RolesAllowed(MotRoles.ADD_ALLEY)
    @Produces(MediaType.TEXT_HTML)
    public String addAlley(@BeanParam Alley alley) {
        throw new UnsupportedOperationException();
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
     * @return Widok z hustorią rozgrywek dla toru.
     */
    @GET
    @RolesAllowed(MotRoles.GET_ALLEY_GAMES_HISTORY)
    @Path("{id}/history")
    @Produces(MediaType.TEXT_HTML)
    public String showGamesHistoryForAlley() {
        throw new UnsupportedOperationException();
    }

    /**
     * Wyświetla najlepszy wynik dla toru
     * @param id identyfikator toru
     * @return Widok z najlepszym wynikiem dla toru
     */
    @GET
    @RolesAllowed(MotRoles.GET_BEST_SCORE_FOR_ALLEY)
    @Path("{id}/best-score")
    @Produces(MediaType.TEXT_HTML)
    public String getBestScoreForAlley(@PathParam("id") Long id) {
        throw new UnsupportedOperationException();
    }

    /**
     * Pobiera wszystkie tory
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
