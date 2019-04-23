package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web;

import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service.RegistrationService;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.User;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.UserAccountDto;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.mvc.Controller;
import javax.mvc.Models;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Set;

/**
 * Klasa odpowiedzialna za mapowanie dla punktów dostępowych związanych z rejestracją użytkowników,
 * takich jak rzeczywisty proces rejestracji oraz weryfikacji.
 */
@RequestScoped
@Controller
@Path("register")
public class RegistrationController {
    @EJB
    private RegistrationService registrationService;

    @Inject
    private Validator validator;

    @Inject
    private Models models;

    /**
     * Punkt wyjścia odpowiedzialny za przekierowanie do widoku z formularzem rejestracji.
     * @return Widok z formularzem rejestracji użytkownika
     */
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String viewRegistrationForm() {
        return "accounts/register/register.hbs";
    }

    /**
     * Punkt wyjścia odpowiedzialny za rejestrację użytkownika oraz przekierowanie do strony o statusie.
     * @param userData DTO przechowujące dane formularza rejestracji.
     * @see UserAccountDto
     * @return Widok potwierdzające rejestrację bądź błąd rejestracji
     */
    @POST
    @Produces(MediaType.TEXT_HTML)
    public String registerAccount(@BeanParam UserAccountDto userData)  {
        Set<ConstraintViolation<UserAccountDto>> violations = validator.validate(userData);
        models.put("data", userData);
        String errorMessage = "";
        for(ConstraintViolation<UserAccountDto> violation : violations) {
            errorMessage += violation.getMessage() + "\n";
        }

        if(!errorMessage.equals("")) {
            models.put("error", errorMessage);
            return "accounts/register/register.hbs";
        }

        if(!userData.getPassword().equals(userData.getConfirmPassword())) {
            models.put("error", "Passwords don't match.");
            return "accounts/register/register.hbs";
        }

        Account account = Account
                .builder()
                .login(userData.getLogin())
                .password(userData.getPassword())
                .confirmed(false)
                .active(true)
                .build();
        User user = User
                .builder()
                .email(userData.getEmail())
                .firstName(userData.getFirstName())
                .lastName(userData.getLastName())
                .phone(userData.getPhoneNumber())
                .build();

        try {
            registrationService.registerAccount(account, user);
        } catch (Exception e) {
            models.put("error", e.getLocalizedMessage() + "\n" + e.getCause());
            return "accounts/register/register.hbs";
        }

        models.put("email", user.getEmail());
        return "accounts/register/register-success.hbs";
    }
}
