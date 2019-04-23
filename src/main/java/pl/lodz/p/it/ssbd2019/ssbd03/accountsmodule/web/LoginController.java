package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web;

import javax.enterprise.context.RequestScoped;
import javax.mvc.Controller;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Controller dotyczący zadań związanych z logowaniem użytkowników.
 */
@RequestScoped
@Controller
@Path("login")
public class LoginController {
    /**
     * Punkt wyjścia odpowiedzialny za przekierowanie do widoku z formularzem logowania.
     * @return Widok strony logowania
     */
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String loginPage() {
        return "accounts/login/login.hbs";
    }
}
