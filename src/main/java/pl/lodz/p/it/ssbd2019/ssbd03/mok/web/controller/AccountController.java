package pl.lodz.p.it.ssbd2019.ssbd03.mok.web.controller;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.conflict.validation.RecaptchaValidationException;
import pl.lodz.p.it.ssbd2019.ssbd03.mok.service.UserAccountService;
import pl.lodz.p.it.ssbd2019.ssbd03.mok.web.dto.NewPasswordWithConfirmationDto;
import pl.lodz.p.it.ssbd2019.ssbd03.mok.web.dto.validators.DtoValidator;
import pl.lodz.p.it.ssbd2019.ssbd03.mok.web.dto.validators.PasswordDtoValidator;
import pl.lodz.p.it.ssbd2019.ssbd03.mok.web.dto.validators.RecaptchaValidator;
import pl.lodz.p.it.ssbd2019.ssbd03.mok.web.rolesretriever.UserRolesRetriever;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.localization.LocalizedMessageProvider;
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

    private static final String ERROR = "errors";
    private static final String INFO = "infos";
    private static final String EDIT_PASSWORD_FORM_HBS = "accounts/edit-password/editByUser.hbs";
    private static final String EDIT_SUCCESS_VIEW = "accounts/edit-password/edit-success.hbs";
    private static final String BASE_URL = "account";
    private static final String DISPLAY_DETAILS = "accounts/users/userOwnDetails.hbs";

    @Inject
    private Models models;
    @Inject
    private DtoValidator validator;

    @Inject
    private PasswordDtoValidator passwordDtoValidator;

    @Inject
    private RedirectUtil redirectUtil;

    @Inject
    private LocalizedMessageProvider localization;

    @Inject
    private RecaptchaValidator recaptchaValidator;

    @EJB
    private UserAccountService userAccountService;


    /**
     * Zwraca widok z danymi zalogowanego użytkownika.
     *
     * @return widok z danymi użytkownika.
     */
    @GET
    @Path("details")
    @RolesAllowed(MokRoles.GET_OWN_ACCOUNT_DETAILS)
    @Produces(MediaType.TEXT_HTML)
    public String displayUserDetails() {
        try {
            String login = (String) models.get("userName");
            UserAccount user = userAccountService.getByLogin(login);
            models.put("user", user);
            UserRolesRetriever.putAccessLevelsIntoModel(user,models);
       } catch (SsbdApplicationException e) {
            displayError(localization.get("detailsRetrievalError"));
        }
        return DISPLAY_DETAILS;
    }



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
        try {
            recaptchaValidator.validateCaptcha(userData.getRecaptcha());
        } catch (RecaptchaValidationException e) {
            errorMessages.add(localization.get("validate.recaptchaNotPerformed"));
        }

        if (!errorMessages.isEmpty()) {
            return redirectUtil.redirectError(BASE_URL + "/edit-password", null, errorMessages);
        }

        try {
            String login = models.get("userName", String.class);
            userAccountService.changePasswordByLogin(login, userData.getCurrentPassword(), userData.getNewPassword());
        } catch (SsbdApplicationException e) {
            return redirectUtil.redirectError(BASE_URL + "/edit-password", null, Collections.singletonList(e.getMessage()));
        }

        models.put(INFO, Collections.singletonList(localization.get("passwordChanged")));
        return redirectSuccessPath();
    }

    private String redirectSuccessPath() {
        return String.format("redirect:%s/edit-password/success", BASE_URL);
    }

    private void displayError(String s) {
        models.put(ERROR, Collections.singletonList(s));
    }

}

