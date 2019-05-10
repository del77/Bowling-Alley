package pl.lodz.p.it.ssbd2019.ssbd03.web.servlets.filters;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.PropertiesLoadException;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.configuration.I18nManager;

import javax.inject.Inject;
import javax.mvc.Models;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Klasa odpowiedzialna za dodawanie elementów strony, które reprezentują treść dla zadanego jezyka.
 */
@WebFilter("/*")
public class LanguageFilter extends HttpFilter {
    @Inject
    private I18nManager i18nManager;

    @Inject
    private Models models;

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        try {
            models.put("lang", i18nManager.getLanguageMap());
        } catch (PropertiesLoadException e) {
            res.sendError(404);
            e.printStackTrace();
        }
        chain.doFilter(req, res);
    }
}
