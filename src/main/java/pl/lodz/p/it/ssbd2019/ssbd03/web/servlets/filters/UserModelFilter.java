package pl.lodz.p.it.ssbd2019.ssbd03.web.servlets.filters;

import javax.inject.Inject;
import javax.mvc.Models;
import javax.servlet.*;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;

/**
 * Klasa odpowiedzialna za dodawanie informacji o użytkowniku w trakcie przechodzenia przez strony.
 * Między innymi takich jak: czy użytkownik jest zalogowany, przynależność do poziomu dostępu czy nazwa (login).
 * Aby filtr działał musi być wpisany w deskryptor web.xml.
 */
public class UserModelFilter extends HttpFilter {
    @Inject
    private Models models;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        Principal userPrincipal = httpServletRequest.getUserPrincipal();
        boolean isLoggedIn = userPrincipal != null;
        boolean isAdmin = httpServletRequest.isUserInRole("ADMIN");
        boolean isEmployee = httpServletRequest.isUserInRole("EMPLOYEE");
        boolean isClient = httpServletRequest.isUserInRole("CLIENT");
        models.put("isAdmin", isAdmin);
        models.put("loggedIn", isLoggedIn);
        models.put("isEmployee", isEmployee);
        models.put("isClient", isClient);
        if (isLoggedIn) {
            models.put("userName", userPrincipal.getName());
        }
        chain.doFilter(request, response);
    }
}
