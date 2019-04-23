package pl.lodz.p.it.ssbd2019.ssbd03.web;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.mvc.Models;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;

public class UserModelFilter implements Filter {
    @Inject
    private Models models;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        Principal userPrincipal = httpServletRequest.getUserPrincipal();
        boolean isLoggedIn = userPrincipal != null;
        boolean isAdmin = httpServletRequest.isUserInRole("ADMIN");
        boolean isEmployee = httpServletRequest.isUserInRole("EMPLOYEE");
        models.put("isAdmin", isAdmin);
        models.put("loggedIn", isLoggedIn);
        models.put("isEmployee", isEmployee);
        if(isLoggedIn) {
            models.put("userName", userPrincipal.getName());
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
