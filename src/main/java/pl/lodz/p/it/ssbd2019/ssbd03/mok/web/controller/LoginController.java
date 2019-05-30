package pl.lodz.p.it.ssbd2019.ssbd03.mok.web.controller;

import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MokRoles;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.SessionScoped;
import javax.mvc.Controller;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.Serializable;

/**
 * Controller dotyczący zadań związanych z logowaniem użytkowników.
 */
@SessionScoped
@Controller
@Path("login")
public class LoginController implements Serializable {
    /**
     * Punkt wyjścia odpowiedzialny za przekierowanie do widoku z formularzem logowania.
     * @return Widok strony logowania
     */
    @GET
    @PermitAll
    @Produces(MediaType.TEXT_HTML)
    public String loginPage() {
        return "accounts/login/login.hbs";
    }

    @GET
    @Path("error")
    @PermitAll
    @Produces(MediaType.TEXT_HTML)
    public String errorPage() {
        return "accounts/login/error.hbs";
    }

    @GET
    @Path("unconfirmed")
    @RolesAllowed(MokRoles.UNCONFIRMED_ACCOUNT)
    @Produces(MediaType.TEXT_HTML)
    public String unconfirmedPage() { return "accounts/login/unconfirmed.hbs"; }
}
