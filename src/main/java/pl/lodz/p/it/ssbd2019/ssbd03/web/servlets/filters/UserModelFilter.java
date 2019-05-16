package pl.lodz.p.it.ssbd2019.ssbd03.web.servlets.filters;

import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.AppRoles;

import javax.inject.Inject;
import javax.mvc.Models;
import javax.servlet.*;
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
    @Inject
    private Models models;

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        Principal userPrincipal = request.getUserPrincipal();
        boolean isLoggedIn = userPrincipal != null;
        boolean isAdmin = request.isUserInRole(AppRoles.ADMIN);
        boolean isEmployee = request.isUserInRole(AppRoles.EMPLOYEE);
        boolean isClient = request.isUserInRole(AppRoles.CLIENT);
        models.put("isAdmin", isAdmin);
        models.put("loggedIn", isLoggedIn);
        models.put("isEmployee", isEmployee);
        models.put("isClient", isClient);
        if (isLoggedIn) {
            models.put("userName", userPrincipal.getName());
        }
        models.put("webContextPath", request.getContextPath());
        models.put("page", request.getRequestURI());
        chain.doFilter(request, response);
    }
}
