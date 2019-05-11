package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web;

import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service.UserAccountService;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.validators.DtoValidator;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.NewPasswordWithConfirmationDto;
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
 * Kontroler odpowiedzialny za obdługę wszystkich operacji związanych
 * z encjami typu UserAccount dla ściezek klienta, tj. dla użytkowników o roli "CLIENT"
 */
@Controller
@RequestScoped
@Path("client/users")
public class UserClientController {

    private static final String ERROR = "errors";
    private static final String INFO = "infos";
    private static final String EDIT_PASSWORD_FORM_HBS = "accounts/edit-password/editByUser.hbs";

    @Inject
    private Models models;

    @Inject
    private DtoValidator validator;
    @Inject
    private PasswordDtoValidator passwordDtoValidator;

    @EJB
    private UserAccountService userAccountService;

    /**
     * Punkt wyjścia odpowiedzialny za przekierowanie do widoku z formularzem edycji hasła.
     *
     * @return Widok z formularzem rejestracji użytkownika
     */
    @GET
    @Path("edit-password")
    @Produces(MediaType.TEXT_HTML)
    public String viewEditPasswordForm() {
        return EDIT_PASSWORD_FORM_HBS;
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
    @Produces(MediaType.TEXT_HTML)
    public String editPassword(@BeanParam NewPasswordWithConfirmationDto userData) {
        List<String> errorMessages = validator.validate(userData);
        errorMessages.addAll(passwordDtoValidator.validatePassword(userData.getNewPassword(), userData.getConfirmNewPassword()));
        errorMessages.addAll(passwordDtoValidator.validateCurrentAndNewPassword(userData.getCurrentPassword(), userData.getNewPassword()));

        if (!errorMessages.isEmpty()) {
            models.put(ERROR, errorMessages);
            return EDIT_PASSWORD_FORM_HBS;
        }

        try {
            String login = (String) models.get("userName");
            userAccountService.changePasswordByLogin(login, userData.getCurrentPassword(), userData.getNewPassword());
        } catch (Exception e) {
            models.put(ERROR, Collections.singletonList(e.getMessage()));
            return EDIT_PASSWORD_FORM_HBS;
        }

        models.put(INFO, Collections.singletonList("Password has been changed."));
        return EDIT_PASSWORD_FORM_HBS;
    }
}
