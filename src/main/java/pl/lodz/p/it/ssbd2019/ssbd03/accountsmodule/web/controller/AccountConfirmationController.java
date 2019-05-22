package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.controller;

import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service.ConfirmationTokenService;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.conflict.AccountAlreadyConfirmedException;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.mvc.Controller;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Klasa kontrolera odpowiedzialnego za akcje związane z potwierdzaniem kont użytkowników.
 */
@Controller
@RequestScoped
@Path("confirm-account")
@PermitAll
public class AccountConfirmationController {

    private static final String FAILURE_PAGE = "accounts/confirm/confirm-failure.hbs";
    private static final String FAILURE_ALREADY_CONFIRMED_PAGE = "accounts/confirm/confirm-failure-ac.hbs";
    private static final String SUCCESS_PAGE = "accounts/confirm/confirm-success.hbs";
    public static final String FAILURE_REDIRECT = "redirect:confirm-account/failure";
    public static final String SUCCESS_REDIRECT = "redirect:confirm-account/success";
    private static final String FAILURE_ALREADY_CONFIRMED = "redirect:confirm-account/failure/already-confirmed";

    @EJB
    private ConfirmationTokenService confirmationTokenService;

    /**
     * Metoda potwierdza konto uzytkownika na bazie tokenu, który powinien dostać.
     * W przypadku, gdy konto zostało już wcześniej potwierdzone zwróci stosowny komunikat użytkownikowi.
     * Podobnie w przypadku błędu nieoczekiwanego.
     * Gdy potwierdzenie się zaś uda, generuje widok suckesu komunikujący o udanej aktywacji.
     * @param token token powiązany z kontem, które nalezy potwierdzić.
     * @return Widok strony błędu badź strony sukcesu
     */
    @GET
    @Path("{token}")
    @Produces(MediaType.TEXT_HTML)
    public String confirmAccount(@PathParam("token") String token) {
        try {
            confirmationTokenService.activateAccountByToken(token);
        } catch (final Exception e) {
            if (e.getCause() != null) {
                if (e.getCause().getClass().equals(AccountAlreadyConfirmedException.class)) {
                    return FAILURE_ALREADY_CONFIRMED;
                }
            }
            return FAILURE_REDIRECT;
        }
        return SUCCESS_REDIRECT;
    }

    @GET
    @Path("failure")
    public String failurePage() {
        return FAILURE_PAGE;
    }

    @GET
    @Path("failure/already-confirmed")
    public String alreadyConfirmedFailurePage() {
        return FAILURE_ALREADY_CONFIRMED_PAGE;
    }

    @GET
    @Path("success")
    public String successPage() {
        return SUCCESS_PAGE;
    }
}