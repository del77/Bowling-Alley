package pl.lodz.p.it.ssbd2019.ssbd03.web.servlets.filters;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.PropertiesLoadException;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.configuration.i18n.I18nManager;

import javax.inject.Inject;
import javax.mvc.Models;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Klasa odpowiedzialna za dodawanie elementów strony, które reprezentują treść dla zadanego jezyka.
 */
@WebFilter(value = "/*", dispatcherTypes = {DispatcherType.ERROR, DispatcherType.REQUEST, DispatcherType.FORWARD})
public class LanguageFilter implements Filter {
    @Inject
    private I18nManager i18nManager;

    @Inject
    private Models models;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
        try {
            models.put("lang", i18nManager.getLanguageMap());
        } catch (PropertiesLoadException e) {
            if (response instanceof HttpServletResponse) {
                ((HttpServletResponse) response).sendError(501);
            }
            e.printStackTrace();
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }

}
