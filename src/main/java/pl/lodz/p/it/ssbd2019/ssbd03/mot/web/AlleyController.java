package pl.lodz.p.it.ssbd2019.ssbd03.mot.web;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Alley;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.mvc.Controller;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Controller
@Path("alleys")
public class AlleyController {

    /**
     * Pobiera widok dodawania toru.
     * @return Widok umożliwiający dodanie toru.
     */
    @GET
    @Path("new")
    @RolesAllowed("AddAlley")
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
    @RolesAllowed("AddAlley")
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
    @RolesAllowed("EnableDisableAlley")
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
    @RolesAllowed("EnableDisableAlley")
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
    @RolesAllowed("GetAlleyGamesHistory")
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
    @RolesAllowed("GetBestScoreForAlley")
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
    @RolesAllowed("GetAlleysList")
    @Produces(MediaType.TEXT_HTML)
    public String GetAllAlleys() {
        throw new UnsupportedOperationException();
    }
}
