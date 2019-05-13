package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web;

import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service.ResetPasswordService;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.EmailDto;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.NewPasswordDto;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.validators.DtoValidator;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.validators.PasswordDtoValidator;

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
 * Controller dotyczący zadań związanych z resetowaniem hasła użytkownika.
 */
@RequestScoped
@Controller
@Path("reset-password")
public class ResetPasswordController {

    private static final String ERROR = "errors";
    private static final String INFO = "infos";
    private static final String REQUEST_FORM_HBS = "accounts/reset-password/requestForm.hbs";
    private static final String RESET_FORM_HBS = "accounts/reset-password/resetForm.hbs";

    @EJB
    private ResetPasswordService resetPasswordService;

    @Inject
    private Models models;

    @Inject
    private DtoValidator validator;

    @Inject
    private PasswordDtoValidator passwordDtoValidator;

    /**
     * @return Formularz żądania resetowania hasła
     */
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String requestPasswordResetForm() {
        return REQUEST_FORM_HBS;
    }

    /**
     * Punkt wyjścia odpowiedzialny za obsłużenie żądania resetowania hasła przez użytkownika.
     *
     * @param userData DTO przechowujące dane z formularza.
     * @return Komunikat potwierdzający żądanie lub błąd
     */
    @POST
    @Produces(MediaType.TEXT_HTML)
    public String requestPasswordReset(@BeanParam EmailDto userData) {
        List<String> errorMessages = validator.validate(userData);

        if (!errorMessages.isEmpty()) {
            models.put(ERROR, errorMessages);
            return REQUEST_FORM_HBS;
        }

        try {
            resetPasswordService.requestResetPassword(userData.getEmail());
        } catch (Exception e) {
            models.put(ERROR, Collections.singletonList(e.getMessage()));
            return REQUEST_FORM_HBS;
        }

        models.put(INFO, Collections.singletonList("Success, check your email!"));
        return REQUEST_FORM_HBS;
    }

    /**
     * @return Formularz resetowania hasła
     */
    @GET
    @Path("/{token}")
    @Produces(MediaType.TEXT_HTML)
    public String resetPasswordForm(@PathParam("token") String token) {
        return RESET_FORM_HBS;
    }

    /**
     * Punkt wyjścia odpowiedzialny za obsłużenie resetowania hasła przez użytkownika.
     *
     * @param userData DTO przechowujące dane z formularza.
     * @param token Unikalny token pozwalający zresetować hasło
     * @return Komunikat potwierdzający żądanie lub błąd
     */
    @POST
    @Path("/{token}")
    @Produces(MediaType.TEXT_HTML)
    public String resetPassword(@BeanParam NewPasswordDto userData, @PathParam("token") String token) {
        List<String> errorMessages = validator.validate(userData);
        errorMessages.addAll(passwordDtoValidator.validatePassword(userData.getNewPassword(), userData.getConfirmNewPassword()));

        if (!errorMessages.isEmpty()) {
            models.put(ERROR, errorMessages);
            return RESET_FORM_HBS;
        }

        try {
            resetPasswordService.resetPassword(token, userData.getNewPassword());
        } catch (Exception e) {
            models.put(ERROR, Collections.singletonList(e.getMessage()));
            return RESET_FORM_HBS;
        }

        models.put(INFO, Collections.singletonList("Your password has been changed!"));
        return RESET_FORM_HBS;
    }
}
