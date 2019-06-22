package pl.lodz.p.it.ssbd2019.ssbd03.web.servlets.filters;


import pl.lodz.p.it.ssbd2019.ssbd03.utils.breadcrumbs.Breadcrumb;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.localization.LocalizedMessageProvider;

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
import java.util.ArrayList;

/**
 * Klasa odpowiedzialna za budowanie modelu do wyświetlania lokalizacji użytkownika na stronie.
 */
@WebFilter(value = "/*", dispatcherTypes = {DispatcherType.REQUEST, DispatcherType.ERROR, DispatcherType.FORWARD})
public class BreadcrumbsFilter extends HttpFilter {

    @Inject
    private Models models;

    @Inject
    private LocalizedMessageProvider localization;

    private String contextPath;

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        contextPath = request.getContextPath();
        if (!isPageRequest(request)) {
            chain.doFilter(request, response);
            return;
        }

        String relativePath = removeContextPathFromUri(request.getRequestURI());

        ArrayList<Breadcrumb> model = new ArrayList<>();
        addBreadcrumbToModel(model, "home", "/", false);

        if (relativePath.matches("/accounts")) {
            addBreadcrumbToModel(model, "accounts", "/accounts", true);
        } else if (relativePath.matches("/accounts/success")) {
            addBreadcrumbToModel(model, "accounts", "/accounts", false);
        } else if (relativePath.matches("/account/details")) {
            addBreadcrumbToModel(model, "profile", "/account/details", true);
        } else if (relativePath.matches("/accounts/\\d+/details(/success)?")) {
            addBreadcrumbToModel(model, "accounts", "/accounts", false);
            addBreadcrumbToModel(model, "details", "#", true);
        } else if (relativePath.matches("/accounts/\\d+/edit")) {
            addBreadcrumbToModel(model, "accounts", "/accounts", false);
            addBreadcrumbToModel(model, "editAccount", "#", true);
        } else if (relativePath.matches("/accounts/\\d+/edit/roles")) {
            addBreadcrumbToModel(model, "accounts", "/accounts", false);
            addBreadcrumbToModel(model, "editAccount", removeLastUriSection(relativePath), false);
            addBreadcrumbToModel(model, "editRoles", "#", true);
        } else if (relativePath.matches("/accounts/\\d+/edit/password")) {
            addBreadcrumbToModel(model, "accounts", "/accounts", false);
            addBreadcrumbToModel(model, "editAccount", removeLastUriSection(relativePath), false);
            addBreadcrumbToModel(model, "editPassword", "#", true);
        } else if (relativePath.matches("/admin/register(/success)?")) {
            addBreadcrumbToModel(model, "createAccount", "/admin/register", true);
        } else if (relativePath.matches("/account/edit-password(/success)?")) {
            addBreadcrumbToModel(model, "editPassword", "/account/edit-password", true);
        } else if (relativePath.matches("/account/edit")) {
            addBreadcrumbToModel(model, "profile", "/account/details", false);
            addBreadcrumbToModel(model, "editOwnAccount", "/account/edit", true);
        } else if (relativePath.matches("/login")) {
            addBreadcrumbToModel(model, "login", "/login", true);
        } else if (relativePath.matches("/register(/success)?")) {
            addBreadcrumbToModel(model, "register", "/register", true);
        } else if (relativePath.matches("/reset-password")) {
            addBreadcrumbToModel(model, "login", "/login", false);
            addBreadcrumbToModel(model, "resetPassword", "#", true);
        } else if (relativePath.matches("/alleys")) {
            addBreadcrumbToModel(model, "alleys", "#", true);
        } else if (relativePath.matches("/myreservations")) {
            addBreadcrumbToModel(model, "ownReservationList", "#", true);
        } else if (relativePath.matches("/myreservations/details/\\d+")) {
            addBreadcrumbToModel(model, "ownReservationList", "/myreservations", false);
            addBreadcrumbToModel(model, "reservationDetails", "#", true);
        } else if (relativePath.matches("/alleys/new")) {
            addBreadcrumbToModel(model, "alleys", "/alleys", false);
            addBreadcrumbToModel(model, "addAlley", "#", true);
        } else if (relativePath.matches("/reservations/user/\\d+")) {
            addBreadcrumbToModel(model, "accounts", "/accounts", false);
            addBreadcrumbToModel(model, "reservations", "#", true);
        } else if (relativePath.matches("/items/balls")) {
            addBreadcrumbToModel(model, "editBallsCount", "/items/balls", true);
        } else if (relativePath.matches("/items/shoes")) {
            addBreadcrumbToModel(model, "editShoesCount", "/items/shoes", true);
        } else if (relativePath.matches("/scores/new/\\d+")) {
            addBreadcrumbToModel(model, "accounts", "/accounts", false);
            addBreadcrumbToModel(model, "addNewScore", "#", true);
        } else if (relativePath.matches("/scores/user/\\d+")) {
            addBreadcrumbToModel(model, "accounts", "/accounts", false);
            addBreadcrumbToModel(model, "scoresHistory", "#", true);
        } else if (relativePath.matches("/reservations/alley/\\d+")) {
            addBreadcrumbToModel(model, "alleys", "/alleys", false);
            addBreadcrumbToModel(model, "reservations", "#", true);
        } else if (relativePath.matches("/employee/servicerequests/new/\\d+")) {
            addBreadcrumbToModel(model, "serviceRequest", "/employee/servicerequests/", false);
            addBreadcrumbToModel(model, "addServiceRequest", "#", true);
        } else if (relativePath.matches("/employee/servicerequests/new/\\d+")) {
            addBreadcrumbToModel(model, "serviceRequest", "/employee/servicerequests/", false);
            addBreadcrumbToModel(model, "addServiceRequest", "#", true);
        } else if (relativePath.matches("/employee/servicerequests/edit/\\d+")) {
            addBreadcrumbToModel(model, "serviceRequest", "/employee/servicerequests/", false);
            addBreadcrumbToModel(model, "editServiceRequest", "#", true);
        } else if (relativePath.matches("/employee/servicerequests(/)?")) {
            addBreadcrumbToModel(model, "serviceRequest", "#", true);
        } else if (relativePath.matches("/myreservations/new")) {
            addBreadcrumbToModel(model, "newReservation", "#", true);
        } else if (relativePath.matches("/reservations/details/\\d+")) {
            addBreadcrumbToModel(model, "reservationDetails", "#", true);
        } else if (relativePath.matches("/comments/add/\\d+")) {
            addBreadcrumbToModel(model, "reservationDetails", "/myreservations/details" + getLastUriSection(relativePath), false);
            addBreadcrumbToModel(model, "addComment", "#", true);
        } else if (relativePath.matches("/reservations/new")) {
            addBreadcrumbToModel(model, "newReservation", "#", true);
        }
        models.put("breadcrumbs", model);
        chain.doFilter(request, response);
    }

    private void addBreadcrumbToModel(ArrayList<Breadcrumb> model, String label, String href, boolean disabled) {
        String pageLabel = localization.get("breadcrumbs." + label);
        String pageHref = contextPath + href;
        model.add(new Breadcrumb(pageLabel, pageHref, disabled));
    }

    private boolean isPageRequest(HttpServletRequest request) {
        return request.getMethod().equals("GET") && !request.getServletPath().equals("/static");
    }

    private String removeLastUriSection(String uri) {
        return uri.substring(0, uri.lastIndexOf('/'));
    }

    private String getLastUriSection(String uri) {
        return uri.substring(uri.lastIndexOf('/'));
    }

    private String removeContextPathFromUri(String uri) {
        return uri.replace(contextPath, "");
    }
}


