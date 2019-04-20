package pl.lodz.p.it.ssbd2019.ssbd03.web.accountsmodule;

import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service.RegistrationService;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.User;
import pl.lodz.p.it.ssbd2019.ssbd03.web.dto.UserAccountDto;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.mvc.Controller;
import javax.mvc.Models;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

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
    private Models models;

    /**
     * Punkt wyjścia odpowiedzialny za przekierowanie do widoku z formularzem rejestracji.
     * @return Widok z formularzem rejestracji użytkownika
     */
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String viewRegistrationForm() {
        return "accounts/register/register.html";
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
        boolean success = true;
        if(!userData.getPassword().equals(userData.getConfirmPassword())) {
            models.put("message", "Passwords don't match.");
            models.put("success", false);
            return "accounts/register/after-register.html";
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
            models.put("message", e.getLocalizedMessage() + "\n" + e.getCause());
            success = false;
        }
        models.put("success", success);
        return "accounts/register/after-register.html";
    }
}
