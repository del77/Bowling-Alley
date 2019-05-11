package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.register;

import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.ComplexAccountDto;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.mappers.DtoMapper;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.mvc.Controller;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Klasa odpowiedzialna za mapowanie dla punktów dostępowych związanych z rejestracją użytkowników,
 * takich jak rzeczywisty proces rejestracji oraz weryfikacji.
 */
@RequestScoped
@Controller
@Path("admin/register")
public class AdminRegistrationController extends RegistrationController {

    private static final String REGISTER_VIEW_URL = "accounts/register/registerByAdmin.hbs";

    @Inject
    private DtoMapper dtoMapper;


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
     * @param complexAccountDto DTO przechowujące dane formularza rejestracji.
     * @return Widok potwierdzający rejestrację bądź błąd rejestracji
     * @see ComplexAccountDto
     */
    @POST
    @Produces(MediaType.TEXT_HTML)
    public String registerAccount(@BeanParam ComplexAccountDto complexAccountDto) {
        return super.registerAccount(complexAccountDto, dtoMapper.getListOfAccessLevels(complexAccountDto));
    }


    @Override
    protected String getRegisterViewUrl() {
        return REGISTER_VIEW_URL;
    }
}
