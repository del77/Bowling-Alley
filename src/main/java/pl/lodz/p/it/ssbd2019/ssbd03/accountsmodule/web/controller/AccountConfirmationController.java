package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.controller;

import pl.lodz.p.it.ssbd2019.ssbd03.utils.localization.LocalizedMessageProvider;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service.ConfirmationTokenService;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.AccountAlreadyConfirmedException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.ConfirmationTokenException;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MokRoles;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.mvc.Controller;
import javax.mvc.Models;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collections;

@Controller
@RequestScoped
@Path("confirm-account")
@PermitAll
public class AccountConfirmationController {

    private static final String FAILURE_PAGE = "accounts/confirm/confirm-failure.hbs";
    private static final String SUCCESS_PAGE = "accounts/confirm/confirm-success.hbs";

    @EJB
    private ConfirmationTokenService confirmationTokenService;

    @Inject
    private LocalizedMessageProvider localization;

    @Inject
    private Models models;

    @GET
    @Path("{token}")
    @Produces(MediaType.TEXT_HTML)
    public String confirmAccount(@PathParam("token") String token) {
        try {
            confirmationTokenService.activateAccountByToken(token);
        } catch (final ConfirmationTokenException e) {
            String error = localization.get("couldntConfirmAccount");
            if (e.getCause() != null) {
                if (e.getCause().getClass().equals(AccountAlreadyConfirmedException.class)) {
                    error += "\n"
                            + localization.get("accountAlreadyConfirmed");
                }
            }
            models.put("error", error);
            return FAILURE_PAGE;
        } catch (final Exception e) {
            models.put("error", Collections.singletonList(localization.get("couldntConfirmAccount")));
            return FAILURE_PAGE;
        }
        return SUCCESS_PAGE;
    }
}
