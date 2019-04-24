package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web;

import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.UserEditPasswordDto;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.Account;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.mvc.Controller;
import javax.mvc.Models;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Kontroler odpowiedzialny za obdługę wszystkich operacji związanych
 * z encjami typu User dla ściezek klienta, tj. dla użytkowników o roli "CLIENT"
 */
@Controller
@RequestScoped
@Path("client/users")
public class UserClientController {
    @Inject
    private Models models;

    @Inject
    private Validator validator;

    /**
     * Punkt wyjścia odpowiedzialny za przekierowanie do widoku z formularzem edycji hasła.
     *
     * @return Widok z formularzem rejestracji użytkownika
     */
    @GET
    @Path("edit-password")
    @Produces(MediaType.TEXT_HTML)
    public String viewRegistrationForm() {
        return "accounts/edit-password/form.hbs";
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
    public String registerAccount(@BeanParam UserEditPasswordDto userData, @Context HttpServletRequest servletRequest) {
        Set<ConstraintViolation<UserEditPasswordDto>> violations = validator.validate(userData);
        List<String> errorMessages = new ArrayList<>();

        for (ConstraintViolation<UserEditPasswordDto> violation : violations) {
            errorMessages.add(violation.getMessage());
        }

        if (!userData.getNewPassword().equals(userData.getConfirmNewPassword())) {
            errorMessages.add("Passwords don't match.");
        }

        if (errorMessages.size() > 0) {
            models.put("errors", errorMessages);
            return "accounts/edit-password/form.hbs";
        }

        // todo: sprawdź czy aktualne hasło jest dobre
        // todo: zaktualizuj hasło na nowe

        return "accounts/edit-password/success.hbs";
    }
}
