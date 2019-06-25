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

@WebFilter(value = "/*", dispatcherTypes = {DispatcherType.REQUEST, DispatcherType.ERROR, DispatcherType.FORWARD})
public class IdFilter extends HttpFilter {
    @Inject
    private Models models;
    
    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        String query = request.getQueryString();
        String idKey = "idFromUri";
        
        if (request.getRequestURI().matches(".*/[\\d+].*")) {
            String[] uriParts = request.getRequestURI().split("/");
            models.put(idKey, uriParts[uriParts.length - 1]);
        } else if (query != null && query.toLowerCase().contains("id")) {
            query = query.toLowerCase();
            int idIndex = query.indexOf("id");
            if (!isItIdCache(query, idIndex)) {
                int start = query.indexOf('=', idIndex) + 1;
                int end = query.indexOf('&', start);
                if (end == -1) {
                    models.put(idKey, query.substring(start));
                } else {
                    models.put(idKey, query.substring(start, end));
                }
            }
        }
        
        chain.doFilter(request, response);
    }
    
    private boolean isItIdCache(String query, int idIndex) {
        return query.length() > idIndex + 7 && query.substring(idIndex, idIndex + 7).equals("idCache");
    }
    
}