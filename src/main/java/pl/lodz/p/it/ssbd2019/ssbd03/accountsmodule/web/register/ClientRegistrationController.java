package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.register;

import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.BasicAccountDto;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.validators.RecaptchaValidator;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.RecaptchaValidationException;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.AppRoles;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.mvc.Controller;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Collections;

/**
 * Klasa odpowiedzialna za mapowanie dla punktów dostępowych związanych z rejestracją użytkowników,
 * takich jak rzeczywisty proces rejestracji oraz weryfikacji.
 */
@RequestScoped
@Controller
@PermitAll
@Path("register")
public class ClientRegistrationController extends RegistrationController {

    @Inject
    private RecaptchaValidator recaptchaValidator;

    private static final String REGISTER_VIEW_URL = "accounts/register/registerClient.hbs";

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
        try {
            recaptchaValidator.validateCaptcha(basicAccountDto.getRecaptcha());
        } catch (RecaptchaValidationException e) {
            errorMessages.add(e.getMessage());
        }
        return super.registerAccount(basicAccountDto, Collections.singletonList(AppRoles.CLIENT));
    }


    @Override
    protected String getRegisterViewUrl() {
        return REGISTER_VIEW_URL;
    }
}
