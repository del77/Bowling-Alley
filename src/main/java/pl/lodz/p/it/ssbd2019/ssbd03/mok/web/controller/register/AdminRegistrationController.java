package pl.lodz.p.it.ssbd2019.ssbd03.mok.web.controller.register;

import pl.lodz.p.it.ssbd2019.ssbd03.mok.web.dto.ComplexAccountDto;
import pl.lodz.p.it.ssbd2019.ssbd03.mok.web.mappers.DtoMapper;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MokRoles;

import javax.annotation.security.RolesAllowed;
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
@RolesAllowed(MokRoles.CREATE_ACCOUNT)
@Path("admin/register")
public class AdminRegistrationController extends RegistrationController {

    private static final String REGISTER_VIEW_URL = "accounts/register/registerByAdmin.hbs";
    private static final String REGISTER_ENDPOINT_URL = "admin/register";

    @Inject
    private DtoMapper dtoMapper;

    /**
     * Punkt wyjścia odpowiedzialny za przekierowanie do widoku z formularzem rejestracji.
     *
     * @return Widok z formularzem rejestracji użytkownika
     */
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String viewRegistrationFormWithFailure(@QueryParam("idCache") Long id) {
        redirectUtil.injectFormDataToModels(id, models);
        return REGISTER_VIEW_URL;
    }

    /**
     * Punkt wyjścia odpowiedzialny za przekierowanie do widoku z komunikatem.
     *
     * @return Widok z komunikatem.
     */
    @GET
    @Path("/success")
    @Produces(MediaType.TEXT_HTML)
    public String viewRegistrationSuccessPage() {
        models.put("infos", Collections.singletonList(localization.get("accountCreated")));
        return this.getSuccessViewUrl();
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
    public String registerAccount(@BeanParam ComplexAccountDto complexAccountDto,
                                  @QueryParam("idCache") Long id) {
        return super.registerAccount(
                complexAccountDto,
                dtoMapper.getListOfAccessLevels(complexAccountDto),
                true
        );
    }

    @Override
    protected String getRegisterEndpointUrl() {
        return REGISTER_ENDPOINT_URL;
    }

    @Override
    protected String getSuccessViewUrl() {
        return REGISTER_VIEW_URL;
    }
}
