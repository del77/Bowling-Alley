package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web;

import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service.RegistrationService;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.BasicAccountDto;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;

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
public class ClientRegistrationController {

    private static final String ERROR_PREFIX = "error";
    private static final String REGISTER_VIEW_URL = "accounts/register/registerClient.hbs";

    @EJB
    private RegistrationService registrationService;

    @Inject
    private DtoValidator validator;

    @Inject
    private Models models;

    /**
     * Punkt wyjścia odpowiedzialny za przekierowanie do widoku z formularzem rejestracji.
     *
     * @return Widok z formularzem rejestracji użytkownika
     */
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String viewRegistrationForm() {
        return REGISTER_VIEW_URL;
    }

    /**
     * Punkt wyjścia odpowiedzialny za rejestrację użytkownika oraz przekierowanie do strony o statusie.
     *
     * @param basicAccountDto DTO przechowujące dane formularza rejestracji.
     * @return Widok potwierdzający rejestrację bądź błąd rejestracji
     * @see BasicAccountDto
     */
    @POST
    @Produces(MediaType.TEXT_HTML)
    public String registerAccount(@BeanParam BasicAccountDto basicAccountDto) {
        String errorMessage = validator.validate(basicAccountDto, models);

        if (!errorMessage.equals("")) {
            models.put(ERROR_PREFIX, errorMessage);
            return REGISTER_VIEW_URL;
        }

        if (!basicAccountDto.getPassword().equals(basicAccountDto.getConfirmPassword())) {
            models.put(ERROR_PREFIX, "Passwords don't match.");
            return REGISTER_VIEW_URL;
        }

        UserAccount userAccount = UserAccount
                .builder()
                .login(basicAccountDto.getLogin())
                .password(basicAccountDto.getPassword())
                .accountConfirmed(false)
                .accountActive(true)
                .email(basicAccountDto.getEmail())
                .firstName(basicAccountDto.getFirstName())
                .lastName(basicAccountDto.getLastName())
                .phone(basicAccountDto.getPhoneNumber())
                .build();

        try {
            registrationService.registerAccount(userAccount, "CLIENT");
        } catch (Exception e) { // TODO more exceptions
            models.put(ERROR_PREFIX, e.getLocalizedMessage() + "\n" + e.getCause());
            return REGISTER_VIEW_URL;
        }

        return "accounts/register/register-success.hbs";
    }

}
