package pl.lodz.p.it.ssbd2019.ssbd03.mot.web;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.mvc.Controller;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Controller
@Path("reservations")
public class ReservationController {
    /**
     * Pobiera widok pozwalający dodać wyniki dla rezerwacji
     * @return Widok z formularzem.
     */
    @GET
    @Path("{id}/enter-result")
    @RolesAllowed("EnterGameResult")
    @Produces(MediaType.TEXT_HTML)
    public String enterResult(@PathParam("id") int id) {
        throw new UnsupportedOperationException();
    }

    /**
     * Wprowadza wyniki do zakończonej rozgrywki
     */
    @POST
    @Path("{id}/enter-result")
    @RolesAllowed("EnterGameResult")
    @Produces(MediaType.TEXT_HTML)
    public String enterResult() {
        throw new UnsupportedOperationException();
    }
}
