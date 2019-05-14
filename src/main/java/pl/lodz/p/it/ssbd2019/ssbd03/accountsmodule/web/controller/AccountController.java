package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.controller;

import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service.UserAccountService;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.validators.DtoValidator;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.NewPasswordWithConfirmationDto;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.validators.PasswordDtoValidator;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.redirect.RedirectUtil;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MokRoles;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.mvc.Controller;
import javax.mvc.Models;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.List;

/**
 * Kontroler odpowiedzialny za obdługę wszystkich operacji związanych
 * z encjami typu UserAccount dla ściezek klienta, tj. dla użytkowników o roli "CLIENT"
 */
@Controller
@RequestScoped
@Path("account")
public class AccountController {

    private static final String INFO = "infos";
    private static final String EDIT_PASSWORD_FORM_HBS = "accounts/edit-password/editByUser.hbs";
    private static final String EDIT_SUCCESS_VIEW = "accounts/edit-password/edit-success.hbs";
    private static final String BASE_URL = "account";

    @Inject
    private Models models;

    @Inject
    private DtoValidator validator;
    @Inject
    private PasswordDtoValidator passwordDtoValidator;
    @Inject
    private RedirectUtil redirectUtil;

    @EJB
    private UserAccountService userAccountService;

    /**
     * Punkt wyjścia odpowiedzialny za przekierowanie do widoku z formularzem edycji hasła.
     *
     * @return Widok z formularzem rejestracji użytkownika
     */
    @GET
    @Path("edit-password")
    @RolesAllowed(MokRoles.CHANGE_OWN_PASSWORD)
    @Produces(MediaType.TEXT_HTML)
    public String viewEditPasswordForm(@QueryParam("idCache") Long idCache) {
        redirectUtil.injectFormDataToModels(idCache, models);
        return EDIT_PASSWORD_FORM_HBS;
    }

    /**
     * Punkt wyjścia odpowiedzialny za przekierowanie do widoku z informacją o udanej zmianie hasła.
     *
     * @return Widoku z informacją o udanej zmianie hasła
     */
    @GET
    @Path("edit-password/success")
    @Produces(MediaType.TEXT_HTML)
    public String viewSuccess() {
        return EDIT_SUCCESS_VIEW;
    }

    /**
     * Punkt wyjścia odpowiedzialny za zmianę hasła użytkownika oraz przekierowanie do strony o statusie.
     *
     * @param userData DTO przechowujące dane formularza edycji hasła.
     * @return Widok potwierdzający aktualizację hasła lub komunikat o błędzie
     * @see NewPasswordWithConfirmationDto
     */
    @POST
    @Path("edit-password")
    @RolesAllowed(MokRoles.CHANGE_OWN_PASSWORD)
    @Produces(MediaType.TEXT_HTML)
    public String editPassword(@BeanParam NewPasswordWithConfirmationDto userData) {
        List<String> errorMessages = validator.validate(userData);
        errorMessages.addAll(passwordDtoValidator.validatePassword(userData.getNewPassword(), userData.getConfirmNewPassword()));
        errorMessages.addAll(passwordDtoValidator.validateCurrentAndNewPassword(userData.getCurrentPassword(), userData.getNewPassword()));

        if (!errorMessages.isEmpty()) {
            return redirectUtil.redirectError(BASE_URL, null, errorMessages);
        }

        try {
            String login = (String) models.get("userName");
            userAccountService.changePasswordByLogin(login, userData.getCurrentPassword(), userData.getNewPassword());
        } catch (Exception e) {
            return redirectUtil.redirectError(BASE_URL, null, Collections.singletonList(e.getMessage()));
        }

        models.put(INFO, Collections.singletonList("Password has been changed."));
        return redirectSuccessPath();
    }

    private String redirectSuccessPath() {
        return String.format("redirect:%s/success", BASE_URL);
    }
}