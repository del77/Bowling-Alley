package pl.lodz.p.it.ssbd2019.ssbd03.web;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.PropertiesLoadException;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.configuration.i18n.I18nManager;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.mvc.Controller;
import javax.mvc.Models;
import javax.mvc.RedirectScoped;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@RequestScoped
@Path("/lang")
public class LanguageController {
    private static String ERROR_SITE = "pages/error.hbs";

    @Inject
    private Models models;

    @Inject
    private I18nManager i18nManager;

    @POST
    @Path("{lang}")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean changeLanguage(@PathParam("lang") String lang) {
        String viewName = (String)models.get("viewName");
        if (lang.equalsIgnoreCase("Polski")) {
            try {
                i18nManager.changeLanguage(I18nManager.POLISH_LOCALE);
            } catch (PropertiesLoadException e) {
                e.printStackTrace();
                return false;
            }
        } else if (lang.equalsIgnoreCase("English")) {
            try {
                i18nManager.changeLanguage(I18nManager.ENGLISH_LOCALE);
            } catch (PropertiesLoadException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
}
