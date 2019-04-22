package pl.lodz.p.it.ssbd2019.ssbd03.web;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.mvc.Controller;
import javax.mvc.Models;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Główny controller. Klasa służy do mapowania view strony głownej bądź też
 * widoków statycznych.
 */
@RequestScoped
@Controller
@Path("")
public class MainController {
    @Inject
    Models models;

    /**
     * Punkt wyjścia odpowiedzialny za przkierowanie do widoku strony głównej.
     * @return Widok, standardowo "index.html"
     */
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String index() {
        return "index.hbs";
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("test")
    public String test() {
        models.put("error", "no error");
        return "accounts/register/register-failure.hbs";
    }
}
