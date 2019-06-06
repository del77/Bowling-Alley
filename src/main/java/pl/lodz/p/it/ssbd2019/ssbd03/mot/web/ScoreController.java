package pl.lodz.p.it.ssbd2019.ssbd03.mot.web;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Alley;
import pl.lodz.p.it.ssbd2019.ssbd03.mok.service.ResetPasswordService;
import pl.lodz.p.it.ssbd2019.ssbd03.mok.web.dto.EmailDto;
import pl.lodz.p.it.ssbd2019.ssbd03.mok.web.dto.validators.DtoValidator;
import pl.lodz.p.it.ssbd2019.ssbd03.mok.web.dto.validators.PasswordDtoValidator;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.service.ScoreService;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.web.dto.ScoreDto;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.localization.LocalizedMessageProvider;
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

@SessionScoped
@Controller
@Path("scores")
public class ScoreController implements Serializable {

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
    @Path("new")
    @RolesAllowed(MotRoles.ADD_SCORE)
    @Produces(MediaType.TEXT_HTML)
    public String addScore(@QueryParam("idCache") Long idCache) {
        // todo
        throw new UnsupportedOperationException();
    }

    /**
     * Dodaje nowy wynik
     *
     * @param score obiekt zawierający informacje o wyniku
     * @return rezultat operacji
     */
    @POST
    @Path("new")
    @RolesAllowed(MotRoles.ADD_SCORE)
    @Produces(MediaType.TEXT_HTML)
    public String addScore(@BeanParam ScoreDto score) {
        // todo
        throw new UnsupportedOperationException();
    }
}
