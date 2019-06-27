package pl.lodz.p.it.ssbd2019.ssbd03.mok.web.controller.register;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.conflict.validation.RecaptchaValidationException;
import pl.lodz.p.it.ssbd2019.ssbd03.mok.web.dto.BasicAccountDto;
import pl.lodz.p.it.ssbd2019.ssbd03.mok.web.dto.validators.RecaptchaValidator;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.AppRolesProvider;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.mvc.Controller;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.Serializable;
import java.util.Collections;

/**
 * Klasa odpowiedzialna za mapowanie dla punktów dostępowych związanych z rejestracją użytkowników,
 * takich jak rzeczywisty proces rejestracji oraz weryfikacji.
 */
@SessionScoped
@Controller
@PermitAll
@Path("register")
public class ClientRegistrationController extends RegistrationController implements Serializable {

    @Inject
    private RecaptchaValidator recaptchaValidator;

    @Inject
    private AppRolesProvider appRolesProvider;

    private static final String REGISTER_VIEW_URL = "accounts/register/registerClient.hbs";
    private static final String SUCCESS_VIEW_URL = "accounts/register/register-success.hbs";
    private static final String REGISTER_ENDPOINT_URL = "register";

    /**
     * Punkt wyjścia odpowiedzialny za przekierowanie do widoku z formularzem rejestracji.
     *
     * @return Widok z formularzem rejestracji użytkownika
     */
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String viewRegistrationForm(@QueryParam("idCache") Long id) {
        redirectUtil.injectFormDataToModels(id, models);
        return REGISTER_VIEW_URL;
    }

    /**
     * Punkt wyjścia odpowiedzialny za przekierowanie do widoku z komunikatem suckesu.
     *
     * @return Widok z komunikatem.
     */
    @GET
    @Path("/success")
    @Produces(MediaType.TEXT_HTML)
    public String viewRegistrationSuccessPage() {
        return this.getSuccessViewUrl();
    }

    /**
     * Punkt wyjścia odpowiedzialny za rejestrację użytkownika oraz przekierowanie do strony o statusie.
     *
     * 1. Użytkownik nie jest zalogowany
     * 2. Użytkownik  klika odnośnik 'zarejestruj' na stronie głównej
     * 3. System wyświetla formularz rejestracji:
     *  login
     *  hasło
     *  powtórz hasło
     *  adres e-mail
     *  imię
     *  nazwisko
     *  telefon
     *  data urodzenia
     * 4. Użytkownik wypełnia formularz
     * 5. Użytkownik zatwierdza wypełnienie formularza
     * 6. System wysyła wiadomość na podany adres e-mail zawierający link do aktywacji konta
     * 7. Użytkownik aktywuje konto poprzez wejście na stronę podaną w wiadomości e-mail
     *
     * @param basicAccountDto DTO przechowujące dane formularza rejestracji.
     * @return Widok potwierdzający rejestrację bądź błąd rejestracji
     * @see BasicAccountDto
     */
    @POST
    @Produces(MediaType.TEXT_HTML)
    public String registerAccount(@BeanParam BasicAccountDto basicAccountDto,
                                  @QueryParam("idCache") Long id) {
        try {
            recaptchaValidator.validateCaptcha(basicAccountDto.getRecaptcha());
        } catch (RecaptchaValidationException e) {
            errorMessages.add(localization.get(e.getCode()));
        }
        return super.registerAccount(basicAccountDto, Collections.singletonList(appRolesProvider.getUnconfirmed()));
    }

    @Override
    protected String getRegisterEndpointUrl() {
        return REGISTER_ENDPOINT_URL;
    }

    @Override
    protected String getSuccessViewUrl() {
        return SUCCESS_VIEW_URL;
    }
}
