package pl.lodz.p.it.ssbd2019.ssbd03.mok.web.controller;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.conflict.validation.RecaptchaValidationException;
import pl.lodz.p.it.ssbd2019.ssbd03.mok.service.UserAccountService;
import pl.lodz.p.it.ssbd2019.ssbd03.mok.web.dto.AccountDetailsDto;
import pl.lodz.p.it.ssbd2019.ssbd03.mok.web.dto.NewPasswordWithConfirmationDto;
import pl.lodz.p.it.ssbd2019.ssbd03.mok.web.dto.validators.DtoValidator;
import pl.lodz.p.it.ssbd2019.ssbd03.mok.web.dto.validators.PasswordDtoValidator;
import pl.lodz.p.it.ssbd2019.ssbd03.mok.web.dto.validators.RecaptchaValidator;
import pl.lodz.p.it.ssbd2019.ssbd03.mok.web.rolesretriever.UserRolesRetriever;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.localization.LocalizedMessageProvider;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.redirect.FormData;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.redirect.RedirectUtil;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MokRoles;

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

/**
 * Kontroler odpowiedzialny za obdługę wszystkich operacji związanych
 * z encjami typu UserAccount dla ściezek klienta, tj. dla użytkowników o roli "CLIENT"
 */
@Controller
@SessionScoped
@Path("account")
public class AccountController implements Serializable {

    private static final String BASE_URL = "account";
    private static final String ERROR = "errors";
    private static final String INFO = "infos";
    private static final String ACCOUNT_DETAILS_VIEW = "accounts/users/userOwnDetails.hbs";
    private static final String EDIT_OWN_ACCOUNT_VIEW = "accounts/users/editOwnDetails.hbs";
    private static final String EDIT_PASSWORD_FORM_VIEW = "accounts/edit-password/editByUser.hbs";
    private static final String EDIT_SUCCESS_VIEW = "accounts/edit-password/edit-success.hbs";
    private static final String EDIT_OWN_ACCOUNT_PATH = "account/edit";

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
            String login = models.get("userName", String.class);
            AccountDetailsDto user = userAccountService.getByLogin(login);
            models.put("user", user);
        } catch (SsbdApplicationException e) {
            displayError(localization.get(e.getCode()));
        }
        return ACCOUNT_DETAILS_VIEW;
    }

    /**
     * Zwraca widok do edycji szczegółów konta zalogowanego użytkownika.
     *
     * @return widok do edycji szczegółów konta zalogowanego użytkownika.
     */
    @GET
    @Path("edit")
    @RolesAllowed(MokRoles.EDIT_OWN_ACCOUNT)
    @Produces(MediaType.TEXT_HTML)
    public String editOwnAccount(@QueryParam("idCache") Long idCache) {
        redirectUtil.injectFormDataToModels(idCache, models);
        try {
            String login = (String) models.get("userName");
            AccountDetailsDto userAccount = userAccountService.getByLogin(login);
            models.put("editedAccount", userAccount);
        } catch (SsbdApplicationException e) {
            displayError(localization.get(e.getCode()));
        }
        return EDIT_OWN_ACCOUNT_VIEW;
    }

    /**
     * Odpowiada za edycję danych własnego konta.
     *
     * @return Informacja o rezultacie edycji.
     */
    @POST
    @Path("edit")
    @RolesAllowed(MokRoles.EDIT_OWN_ACCOUNT)
    @Produces(MediaType.TEXT_HTML)
    public String editUser(@BeanParam AccountDetailsDto accountDetailsDto) {
        List<String> errorMessages = validator.validate(accountDetailsDto);

        try {
            recaptchaValidator.validateCaptcha(accountDetailsDto.getRecaptcha());
        } catch (RecaptchaValidationException e) {
            errorMessages.add(localization.get(e.getCode()));
        }

        if (!errorMessages.isEmpty()) {
            return redirectUtil.redirectError(
                    EDIT_OWN_ACCOUNT_PATH,
                    accountDetailsDto,
                    errorMessages);
        }

        try {
            userAccountService.updateUser(accountDetailsDto);
        } catch (SsbdApplicationException e) {
            return redirectUtil.redirectError(
                    EDIT_OWN_ACCOUNT_PATH,
                    accountDetailsDto,
                    Collections.singletonList(localization.get(e.getCode()))
            );
        }

        FormData formData = FormData.builder()
                .data(accountDetailsDto)
                .infos(Collections.singletonList(
                        localization.get("accountDetailsUpdated")
                ))
                .build();
        return redirectUtil.redirect(EDIT_OWN_ACCOUNT_PATH, formData);
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
        return EDIT_PASSWORD_FORM_VIEW;
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
            return redirectUtil.redirectError(BASE_URL + "/edit-password", null, Collections.singletonList(localization.get(e.getCode())));
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

