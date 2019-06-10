package pl.lodz.p.it.ssbd2019.ssbd03.mot.web;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.mok.web.dto.validators.DtoValidator;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.service.ItemService;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.web.dto.ItemDto;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.localization.LocalizedMessageProvider;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.redirect.FormData;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.redirect.RedirectUtil;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MotRoles;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.mvc.Controller;
import javax.mvc.Models;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SessionScoped
@Controller
@Path("items")
public class ItemsController implements Serializable {

    // ================ PATHS =========================
    private static final String EDIT_BALLS_COUNT_PATH = "items/balls";
    private static final String EDIT_SHOES_COUNT_PATH = "items/shoes";

    // ================= VIEWS ========================
    private static final String EDIT_BALLS_VIEW = "mot/edit-items-count/balls.hbs";
    private static final String EDIT_SHOES_VIEW = "mot/edit-items-count/shoes.hbs";

    @EJB
    private ItemService itemService;

    @Inject
    private Models models;
    @Inject
    private DtoValidator validator;
    @Inject
    private RedirectUtil redirectUtil;
    @Inject
    private LocalizedMessageProvider localization;

    /**
     * Zwraca widok pozwalający edytować liczbę kul
     * @param idCache identyfikator zapamiętanego stanu formularza w cache
     * @return Widok z formularzem wypełnionym aktualnymi danymi
     */
    @GET
    @Path("balls")
    @RolesAllowed(MotRoles.EDIT_BALLS_COUNT)
    @Produces(MediaType.TEXT_HTML)
    public String editBallsCount(@QueryParam("idCache") Long idCache) {
        redirectUtil.injectFormDataToModels(idCache, models);
        List<ItemDto> balls = itemService.getItemsBySpecifiedItemType("ball");
        models.put("items", balls);
        return EDIT_BALLS_VIEW;
    }

    /**
     * Aktualizuje liczbę wszystkich kul
     * @param sizes lista rozmiarów kul
     * @param counts lista liczebności kul
     * @param idCache identyfikator zapamiętanego stanu formularza w cache
     * @return widok prezentujący rezultat operacji
     */
    @POST
    @Path("balls")
    @RolesAllowed(MotRoles.EDIT_BALLS_COUNT)
    @Produces(MediaType.TEXT_HTML)
    public String editBallsCount(@FormParam("size") List<Integer> sizes, @FormParam("count") List<Integer> counts, @QueryParam("idCache") Long idCache) {
        List<ItemDto> balls = getItemDtoListFromFormParams(sizes, counts);

        List<String> errorMessages = validator.validateAll(balls);

        if (!errorMessages.isEmpty()) {
            return redirectUtil.redirectError(
                    EDIT_BALLS_COUNT_PATH,
                    balls,
                    errorMessages);
        }

        try {
            itemService.updateItems(balls);
        } catch (SsbdApplicationException e) {
            return redirectUtil.redirectError(
                    EDIT_BALLS_COUNT_PATH,
                    balls,
                    Collections.singletonList(localization.get(e.getCode()))
            );
        }

        FormData formData = FormData.builder()
                .data(balls)
                .infos(Collections.singletonList(
                        localization.get("ballsQuantityUpdated")
                ))
                .build();
        return redirectUtil.redirect(EDIT_BALLS_COUNT_PATH, formData);
    }

    /**
     * Zwraca widok pozwalający edytować liczbę par butów
     * @param idCache identyfikator zapamiętanego stanu formularza w cache
     * @return Widok z formularzem wypełnionym aktualnymi danymi
     */
    @GET
    @Path("shoes")
    @RolesAllowed(MotRoles.EDIT_BALLS_COUNT)
    @Produces(MediaType.TEXT_HTML)
    public String editShoesCount(@QueryParam("idCache") Long idCache) {
        redirectUtil.injectFormDataToModels(idCache, models);
        List<ItemDto> shoes = itemService.getItemsBySpecifiedItemType("shoes");
        models.put("items", shoes);
        return EDIT_SHOES_VIEW;
    }

    /**
     * Aktualizuje liczbę wszystkich par butów
     * @param sizes lista rozmiarów butów
     * @param counts lista liczebności butów
     * @param idCache identyfikator zapamiętanego stanu formularza w cache
     * @return widok prezentujący rezultat operacji
     */
    @POST
    @Path("shoes")
    @RolesAllowed(MotRoles.EDIT_SHOES_COUNT)
    @Produces(MediaType.TEXT_HTML)
    public String editShoesCount(@FormParam("size") List<Integer> sizes, @FormParam("count") List<Integer> counts, @QueryParam("idCache") Long idCache) {
        List<ItemDto> shoes = getItemDtoListFromFormParams(sizes, counts);

        List<String> errorMessages = validator.validateAll(shoes);

        if (!errorMessages.isEmpty()) {
            return redirectUtil.redirectError(
                    EDIT_SHOES_COUNT_PATH,
                    shoes,
                    errorMessages);
        }

        try {
            itemService.updateItems(shoes);
        } catch (SsbdApplicationException e) {
            return redirectUtil.redirectError(
                    EDIT_SHOES_COUNT_PATH,
                    shoes,
                    Collections.singletonList(localization.get(e.getCode()))
            );
        }

        FormData formData = FormData.builder()
                .data(shoes)
                .infos(Collections.singletonList(
                        localization.get("shoesQuantityUpdated")
                ))
                .build();
        return redirectUtil.redirect(EDIT_SHOES_COUNT_PATH, formData);
    }

    /**
     * Metoda pomocnicza tworząca listę Dto na podstawie danych otrzymanych z formularza
     * @param sizes rozmiary przedmiotów
     * @param counts liczebności przedmiotów
     * @return lista Dto przedmiotów
     */
    private List<ItemDto>getItemDtoListFromFormParams(List<Integer> sizes, List<Integer> counts) {
        List<ItemDto> items = new ArrayList<>();
        for(int i=0;i<sizes.size();i++) {
            items.add(new ItemDto(sizes.get(i), counts.get(i)));
        }
        return items;
    }

}
