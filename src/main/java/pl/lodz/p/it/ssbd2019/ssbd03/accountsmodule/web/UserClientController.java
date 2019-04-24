package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web;

import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository.AccountRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.UserEditPasswordDto;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.SHA256Provider;

import javax.ejb.EJB;
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
import java.util.Optional;
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

    @EJB(beanName = "MOKAccountRepository")
    AccountRepositoryLocal accountRepositoryLocal;

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

        if (errorMessages.size() > 0) {
            models.put("errors", errorMessages);
            return "accounts/edit-password/form.hbs";
        }

        try {
            Account account = accountRepositoryLocal.findById(4L).get(); // todo: currentUserId
            String oldPasswordHash = SHA256Provider.encode(userData.getCurrentPassword());
            String newPasswordHash = SHA256Provider.encode(userData.getNewPassword());

            if (!userData.getNewPassword().equals(userData.getConfirmNewPassword())) {
                throw new Exception("Passwords don't match.");
            }

            if (!oldPasswordHash.equals(account.getPassword())) {
                throw new Exception("Current password is incorrect.");
            }

            account.setPassword(newPasswordHash);
            accountRepositoryLocal.edit(account);
        } catch (Exception e) {
            errorMessages.add(e.getMessage());
            models.put("errors", errorMessages);
            return "accounts/edit-password/form.hbs";
        }

        return "accounts/edit-password/success.hbs";
    }
}
