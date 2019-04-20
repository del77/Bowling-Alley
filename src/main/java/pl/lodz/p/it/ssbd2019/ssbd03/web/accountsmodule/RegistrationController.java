package pl.lodz.p.it.ssbd2019.ssbd03.web.accountsmodule;

import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service.RegistrationService;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.User;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityRetrievelException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.RegistrationProcessException;
import pl.lodz.p.it.ssbd2019.ssbd03.web.dto.UserAccountDto;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.mvc.Controller;
import javax.mvc.Models;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Controller
@Path("register")
public class RegistrationController {
    @Inject
    private RegistrationService registrationService;

    @Inject
    private Models models;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String viewRegistrationForm() {
        return "accounts/register/register.html";
    }

    @POST
    @Produces(MediaType.TEXT_HTML)
    public String registerAccount(@BeanParam UserAccountDto userData)  {
        boolean success = true;
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
            models.put("message", e.getMessage());
            success = false;
        }
        models.put("success", success);
        return "accounts/register/after-register.html";
    }
}
