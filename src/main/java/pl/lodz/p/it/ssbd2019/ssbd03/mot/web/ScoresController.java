package pl.lodz.p.it.ssbd2019.ssbd03.mot.web;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.mvc.Controller;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Controller
public class ScoresController {
    /**
     * Pobiera wyniki osiągnięte przez klienta
     */
    @GET
    @Path("account/{id}/history")
    @RolesAllowed("ShowUserScoreHistory")
    @Produces(MediaType.TEXT_HTML)
    public String showUserScoreHistory(@PathParam("id") int id) {
        throw new UnsupportedOperationException();
    }
}
