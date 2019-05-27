package pl.lodz.p.it.ssbd2019.ssbd03.web.servlets.filters;


import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.DataAccessException;
import pl.lodz.p.it.ssbd2019.ssbd03.mok.repository.UserAccountRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.AppRoles;

import javax.ejb.EJB;
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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Klasa odpowiedzialna za dodawanie informacji o użytkowniku w trakcie przechodzenia przez strony.
 * Między innymi takich jak: czy użytkownik jest zalogowany, przynależność do poziomu dostępu czy nazwa (login).
 * Aby filtr działał musi być wpisany w deskryptor web.xml.
 * Dodaje również informacje o ściezkach.
 */
@WebFilter(value = "/*", dispatcherTypes = {DispatcherType.REQUEST, DispatcherType.ERROR, DispatcherType.FORWARD})
public class UserModelFilter extends HttpFilter {

    private static final Logger logger = Logger.getLogger(UserModelFilter.class.getName());

    @Inject
    private Models models;

    @EJB(beanName = "MOKUserRepository")
    UserAccountRepositoryLocal userAccountRepositoryLocal;

    private static final String DATE_PATTERN = "MM/dd/yyyy HH:mm:ss";

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
            String userName = userPrincipal.getName();
            tryFillModelsWithLoginStat(userName);
            models.put("userName", userName);
        }

        models.put("webContextPath", request.getContextPath());
        models.put("page", request.getRequestURI());
        chain.doFilter(request, response);
    }

    private void tryFillModelsWithLoginStat(String userName) {
        try {
            fillModelsWithLoginStat(userName);
        } catch (DataAccessException e) {
            logger.severe(e.getLocalizedMessage());
        }
    }

    private void fillModelsWithLoginStat(String userName) throws DataAccessException {
        Optional<UserAccount> userAccountOptional = userAccountRepositoryLocal.findByLogin(userName);

        if (userAccountOptional.isPresent()) {
            UserAccount userAccount = userAccountOptional.get();
            String successfulLoginDate= retrieveSuccessfulLoginDate(userAccount);
            String failedLoginDate = retrieveFailedLoginDate(userAccount);
            models.put("successfulLoginDate", successfulLoginDate);
            models.put("failedLoginDate", failedLoginDate);
        }
    }

    private String retrieveFailedLoginDate(UserAccount userAccount) {
        Timestamp timestamp = userAccount.getLastFailedLogin();
        return formatTimestamp(timestamp);
    }

    private String retrieveSuccessfulLoginDate(UserAccount userAccount) {
        Timestamp timestamp = userAccount.getLastSuccessfulLogin();
        return formatTimestamp(timestamp);
    }

    private String formatTimestamp(Timestamp timestamp) {
        if (timestamp != null) {
            return new SimpleDateFormat(DATE_PATTERN).format(timestamp);
        } else {
            return "--|--";
        }
    }
}
