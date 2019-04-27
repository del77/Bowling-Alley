package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web;

import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service.UserAccountService;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityRetrievalException;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.mvc.Controller;
import javax.mvc.Models;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
}
