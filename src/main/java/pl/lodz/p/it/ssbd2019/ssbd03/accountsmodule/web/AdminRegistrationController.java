package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web;

import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service.RegistrationService;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.ComplexAccountDto;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityRetrievalException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.NotUniqueParameterException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.RegistrationProcessException;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.mvc.Controller;
import javax.mvc.Models;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Controller
@Path("admin/register")
public class AdminRegistrationController {

    private static final String ERROR_PREFIX = "error";
    private static final String REGISTER_VIEW_URL = "accounts/register/registerByAdmin.hbs";

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
     * @param complexAccountDto DTO przechowujące dane formularza rejestracji.
     * @return Widok potwierdzający rejestrację bądź błąd rejestracji
     * @see ComplexAccountDto
     */
    @POST
    @Produces(MediaType.TEXT_HTML)
    public String registerAccount(@BeanParam ComplexAccountDto complexAccountDto) {
        String errorMessage = validator.validate(complexAccountDto, models);

        if (!errorMessage.equals("")) {
            return handleException(errorMessage);
        }

        if (!complexAccountDto.getPassword().equals(complexAccountDto.getConfirmPassword())) {
            return handleException("Passwords don't match.");
        }

        UserAccount userAccount = UserAccount
                .builder()
                .login(complexAccountDto.getLogin())
                .password(complexAccountDto.getPassword())
                .accountConfirmed(false)
                .accountActive(true)
                .email(complexAccountDto.getEmail())
                .firstName(complexAccountDto.getFirstName())
                .lastName(complexAccountDto.getLastName())
                .phone(complexAccountDto.getPhoneNumber())
                .build();

        try {
            registrationService.registerAccount(userAccount, complexAccountDto.getAccessLevelValue());
        } catch (NotUniqueParameterException e) {
            return handleException("Your email or login is not unique.");
        }
        catch (RegistrationProcessException | EntityRetrievalException e) {
            return handleException(e.getMessage());
        } catch (Exception e) {
            models.put(ERROR_PREFIX, e.getLocalizedMessage() + "\n" + e.getCause());
            return REGISTER_VIEW_URL;
        }

        return "accounts/register/register-success.hbs";
    }

    private String handleException(String message) {
        models.put(ERROR_PREFIX, message);
        return REGISTER_VIEW_URL;
    }

}
