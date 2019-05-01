package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web;

import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service.UserAccountService;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.UserEditPasswordDto;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.mvc.Controller;
import javax.mvc.Models;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Kontroler odpowiedzialny za obdługę wszystkich operacji związanych
 * z encjami typu UserAccount dla ściezek klienta, tj. dla użytkowników o roli "CLIENT"
 */
@Controller
@RequestScoped
@Path("client/users")
public class UserClientController {
    @Inject
    private Models models;

    @Inject
    private DtoValidator validator;

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
    public String viewRegistrationForm() {
        return "accounts/edit-password/editByUser.hbs";
    }

    /**
     * Punkt wyjścia odpowiedzialny za zmianę hasła użytkownika oraz przekierowanie do strony o statusie.
     *
     * @param userData DTO przechowujące dane formularza edycji hasła.
     * @return Widok potwierdzający aktualizację hasła lub komunikat o błędzie
     * @see UserEditPasswordDto
     */
    @POST
    @Path("edit-password")
    @Produces(MediaType.TEXT_HTML)
    public String registerAccount(@BeanParam UserEditPasswordDto userData) {
        List<String> errorMessages = validator.validate(userData);
        errorMessages.addAll(validator.validatePasswordsEquality(userData.getNewPassword(), userData.getConfirmNewPassword()));

        if (!errorMessages.isEmpty()) {
            models.put("errors", errorMessages);
            return "accounts/edit-password/editByUser.hbs";
        }

        try {
            String login = (String) models.get("userName");
            userAccountService.changePassword(login, userData.getCurrentPassword(), userData.getNewPassword());
        } catch (Exception e) {
            errorMessages.add(e.getMessage());
            models.put("errors", errorMessages);
            return "accounts/edit-password/editByUser.hbs";
        }

        return "accounts/edit-password/success.hbs";
    }
}
