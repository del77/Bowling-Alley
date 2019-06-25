package pl.lodz.p.it.ssbd2019.ssbd03.web.servlets.filters;

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

@WebFilter(urlPatterns = {"/myreservations/*", "/reservations/*"}, dispatcherTypes = {DispatcherType.REQUEST, DispatcherType.ERROR, DispatcherType.FORWARD})
public class ReservationFilter extends HttpFilter {
    
    @Inject
    private Models models;
    
    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        models.put("reservationContext", request.getRequestURI().replace(request.getContextPath(), "").split("/")[1]);
        chain.doFilter(request, response);
    }
}
