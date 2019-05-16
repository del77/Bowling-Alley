package pl.lodz.p.it.ssbd2019.ssbd03.mot.web;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.ItemType;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MotRoles;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.mvc.Controller;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@RequestScoped
@Controller
@Path("items")
public class ItemsController {

    /**
     * Zwraca widok pozwalający edytować liczbę kul
     * @return Widok z formularzem wypełnionym aktualnymi danymi
     */
    @GET
    @Path("balls")
    @RolesAllowed(MotRoles.EDIT_BALLS_COUNT)
    @Produces(MediaType.TEXT_HTML)
    public String editBallsCount() {
        throw new UnsupportedOperationException();
    }

    /**
     * Aktualizuje stan kól
     */
    @POST
    @Path("balls")
    @RolesAllowed(MotRoles.EDIT_BALLS_COUNT)
    @Produces(MediaType.TEXT_HTML)
    public String editBallsCount(@BeanParam List<ItemType> balls) {
        throw new UnsupportedOperationException();
    }

    /**
     * Zwraca widok pozwalający edytować liczbę butów
     * @return Widok z formularzem wypełnionym aktualnymi danymi
     */
    @GET
    @Path("shoes")
    @RolesAllowed(MotRoles.EDIT_SHOES_COUNT)
    @Produces(MediaType.TEXT_HTML)
    public String editShoesCount() {
        throw new UnsupportedOperationException();
    }

    /**
     * Aktualizuje stan butów
     */
    @POST
    @Path("shoes")
    @RolesAllowed(MotRoles.EDIT_SHOES_COUNT)
    @Produces(MediaType.TEXT_HTML)
    public String editShoesCount(@BeanParam List<ItemType> shoes) {
        throw new UnsupportedOperationException();
    }



}
