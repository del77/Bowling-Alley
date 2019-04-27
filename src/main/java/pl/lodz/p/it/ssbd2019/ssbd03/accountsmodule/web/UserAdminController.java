package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web;

import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service.UserAccountService;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityRetrievalException;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.mvc.Controller;
import javax.mvc.Models;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

/**
 * Kontroler odpowiedzialny za obdługę wszystkich operacji związanych z encjami typu UserAccount dla
 * ściezek admina, tj. dla użytkowników o roli "ADMIN"
 */
@Controller
@RequestScoped
@Path("admin/users")
public class UserAdminController {
    @Inject
    private Models models;

    @EJB
    private UserAccountService userAccountService;

    /**
     * Zwraca widok z listą wszystkich użytkowników. W wypadku wystąpienia błędu lista jest pusta
     * a użytkownik widzi błąd.
     *
     * @return Widok z listą wszystkich użytkowników.
     */
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String allUsersList() {
        List<UserAccount> userAccounts = new ArrayList<>();
        try {
            userAccounts = userAccountService.getAllUsers();
        } catch (EntityRetrievalException e) {
            models.put("error", "Could not retrieve list of userAccounts.\n" + e.getLocalizedMessage());
        }
        models.put("userAccounts", userAccounts);
        return "accounts/users/userslist.hbs";
    }
    
    /**
     * Odblokowuje konto użytkownika z podanym identyfikatorem i zwraca true, jeśli operacja się powiedzie
     *
     * @param id id konta, które należy odblokować
     * @return true, jeśli odblokowanie konta się powiedzie
     */
    @PUT
    @Path("unlock/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Boolean unlockAccount(@PathParam("id") Long id) {
        try {
            userAccountService.unlockAccountById(id);
        } catch (Exception e) {
            models.put("error", "Could not unlock user's account.\n" + e.getLocalizedMessage());
            return false;
        }
        return true;
    }
}
