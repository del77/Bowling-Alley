package pl.lodz.p.it.ssbd2019.ssbd03.mok.web.controller;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.mok.service.ResetPasswordService;
import pl.lodz.p.it.ssbd2019.ssbd03.mok.web.dto.EmailDto;
import pl.lodz.p.it.ssbd2019.ssbd03.mok.web.dto.NewPasswordDto;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.DtoValidator;
import pl.lodz.p.it.ssbd2019.ssbd03.mok.web.dto.validators.PasswordDtoValidator;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.localization.LocalizedMessageProvider;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.redirect.FormData;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.redirect.RedirectUtil;

import javax.annotation.security.PermitAll;
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
 * Controller dotyczący zadań związanych z resetowaniem hasła użytkownika.
 */
@SessionScoped
@Controller
@Path("reset-password")
public class ResetPasswordController implements Serializable {

    private static final String RESET_PASSWORD_URL = "/reset-password";
    private static final String REQUEST_FORM_HBS = "accounts/reset-password/requestForm.hbs";
    private static final String RESET_FORM_HBS = "accounts/reset-password/resetForm.hbs";

    @EJB
    private ResetPasswordService resetPasswordService;

    @Inject
    private Models models;

    @Inject
    private DtoValidator validator;

    @Inject
    private RedirectUtil redirectUtil;

    @Inject
    private PasswordDtoValidator passwordDtoValidator;

    @Inject
    private LocalizedMessageProvider localization;

    /**
     * @return Formularz żądania resetowania hasła
     */
    @GET
    @PermitAll
    @Produces(MediaType.TEXT_HTML)
    public String requestPasswordResetForm(@QueryParam("idCache") Long idCache) {
        redirectUtil.injectFormDataToModels(idCache, models);
        return REQUEST_FORM_HBS;
    }

    /**
     * Punkt wyjścia odpowiedzialny za obsłużenie żądania resetowania hasła przez użytkownika.
     *
     * @param userData DTO przechowujące dane z formularza.
     * @return Komunikat potwierdzający żądanie lub błąd
     */
    @POST
    @PermitAll
    @Produces(MediaType.TEXT_HTML)
    public String requestPasswordReset(@BeanParam EmailDto userData) {
        List<String> errorMessages = validator.validate(userData);
        if (!errorMessages.isEmpty()) {
            return redirectUtil.redirectError(RESET_PASSWORD_URL, null, errorMessages);
        }

        try {
            resetPasswordService.requestResetPassword(userData.getEmail());
        } catch (SsbdApplicationException e) {
            return redirectUtil.redirectError(RESET_PASSWORD_URL, null, Collections.singletonList(localization.get(e.getCode())));
        }

        FormData formData = new FormData();
        formData.setInfos(Collections.singletonList(localization.get("emailHasBeenSent")));
        return redirectUtil.redirect(RESET_PASSWORD_URL, formData);
    }

    /**
     * @return Formularz resetowania hasła
     */
    @GET
    @PermitAll
    @Path("/{token}")
    @Produces(MediaType.TEXT_HTML)
    public String resetPasswordForm(@PathParam("token") String token, @QueryParam("idCache") Long idCache) {
        redirectUtil.injectFormDataToModels(idCache, models);
        return RESET_FORM_HBS;
    }

    /**
     * Punkt wyjścia odpowiedzialny za obsłużenie resetowania hasła przez użytkownika.
     *
     * @param userData DTO przechowujące dane z formularza.
     * @param token    Unikalny token pozwalający zresetować hasło
     * @return Komunikat potwierdzający żądanie lub błąd
     */
    @POST
    @PermitAll
    @Path("/{token}")
    @Produces(MediaType.TEXT_HTML)
    public String resetPassword(@BeanParam NewPasswordDto userData, @PathParam("token") String token) {
        List<String> errorMessages = validator.validate(userData);
        errorMessages.addAll(passwordDtoValidator.validatePassword(userData.getNewPassword(), userData.getConfirmNewPassword()));

        if (!errorMessages.isEmpty()) {
            return redirectUtil.redirectError(RESET_PASSWORD_URL + "/" + token, null, errorMessages);
        }

        try {
            resetPasswordService.resetPassword(token, userData.getNewPassword());
        } catch (SsbdApplicationException e) {
            return redirectUtil.redirectError(RESET_PASSWORD_URL + "/" + token, null, Collections.singletonList(localization.get(e.getCode())));
        }

        FormData formData = new FormData();
        formData.setInfos(Collections.singletonList(localization.get("passwordChangedSuccess")));
        return redirectUtil.redirect(RESET_PASSWORD_URL, formData);
    }
}
