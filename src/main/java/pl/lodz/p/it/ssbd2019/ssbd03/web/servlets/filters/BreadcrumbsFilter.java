package pl.lodz.p.it.ssbd2019.ssbd03.web.servlets.filters;

import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.localization.LocalizedMessageRetriever;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.breadcrumbs.Breadcrumb;

import javax.servlet.annotation.WebFilter;
import javax.inject.Inject;
import javax.mvc.Models;
import javax.servlet.DispatcherType;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
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
    private LocalizedMessageRetriever localization;

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
        } else if (relativePath.matches("/accounts/\\d+/edit/password")) {
            addBreadcrumbToModel(model, "accounts", "/accounts", false);
            addBreadcrumbToModel(model, "editAccount", removeLastUriSection(relativePath), false);
            addBreadcrumbToModel(model, "editPassword", "#", true);
        } else if (relativePath.matches("/admin/register(/success)?")) {
            addBreadcrumbToModel(model, "createAccount", "/admin/register", true);
        } else if (relativePath.matches("/account/edit-password")) {
            addBreadcrumbToModel(model, "editPassword", "/account/edit-password", true);
        } else if (relativePath.matches("/login")) {
            addBreadcrumbToModel(model, "login", "/login", true);
        } else if (relativePath.matches("/register(/success)?")) {
            addBreadcrumbToModel(model, "register", "/register", true);
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

    private String removeContextPathFromUri(String uri) {
        return uri.replace(contextPath, "");
    }
}


