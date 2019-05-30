package pl.lodz.p.it.ssbd2019.ssbd03.web.servlets.filters;


import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.AppRolesProvider;

import javax.inject.Inject;
import javax.mvc.Models;
import javax.servlet.DispatcherType;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

/**
 * Klasa odpowiedzialna za dodawanie informacji o użytkowniku w trakcie przechodzenia przez strony.
 * Między innymi takich jak: czy użytkownik jest zalogowany, przynależność do poziomu dostępu czy nazwa (login).
 * Aby filtr działał musi być wpisany w deskryptor web.xml.
 * Dodaje również informacje o ściezkach.
 */
@WebFilter(value = "/*", dispatcherTypes = {DispatcherType.REQUEST, DispatcherType.ERROR, DispatcherType.FORWARD})
public class UserModelFilter extends HttpFilter {
    private static final String unconfirmedPage = "login/unconfirmed";
    private static final String logoutPage = "logout";

    @Inject
    private AppRolesProvider appRolesProvider;

    @Inject
    private Models models;

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        Principal userPrincipal = request.getUserPrincipal();
        boolean isLoggedIn = userPrincipal != null;
        boolean isAdmin = request.isUserInRole(appRolesProvider.getAdmin());
        boolean isEmployee = request.isUserInRole(appRolesProvider.getEmployee());
        boolean isClient = request.isUserInRole(appRolesProvider.getClient());

        models.put("isAdmin", isAdmin);
        models.put("loggedIn", isLoggedIn);
        models.put("isEmployee", isEmployee);
        models.put("isClient", isClient);

        if (isLoggedIn) {
            String userName = userPrincipal.getName();
            models.put("userName", userName);
            if(shouldRedirectToUnconfirmed(request)) {
                response.sendRedirect(request.getContextPath()+ "/" + unconfirmedPage);
            }
        }

        models.put("webContextPath", request.getContextPath());
        models.put("page", request.getRequestURI());
        chain.doFilter(request, response);
    }

    /**
     * Metoda określająca czy użytkownik powinien zostać przekierowany na stronę dla użytkownikow
     * z niepotwierdzonym kontem.
     */
    private boolean shouldRedirectToUnconfirmed(HttpServletRequest request) {
        String path = request.getRequestURI().substring(request.getContextPath().length()+1);
        return (request.isUserInRole(appRolesProvider.getUnconfirmed()) &&
                !path.startsWith("static") &&
                !path.startsWith("confirm-account") &&
                !path.endsWith(unconfirmedPage) &&
                !path.endsWith(logoutPage));
    }
}
