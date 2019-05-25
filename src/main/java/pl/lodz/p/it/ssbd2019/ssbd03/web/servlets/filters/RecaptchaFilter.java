package pl.lodz.p.it.ssbd2019.ssbd03.web.servlets.filters;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.generalized.PropertiesLoadException;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.RecaptchaKeysProvider;

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

/**
 * Klasa odpowiedzialna za dostarczenie do wybranych formularzy klucza niezbędnego do wyświetlenie modułu recaptcha.
 */
@WebFilter(value = {"/register", "/account/edit-password", "/account/edit"}, dispatcherTypes = {DispatcherType.ERROR, DispatcherType.REQUEST, DispatcherType.FORWARD})
public class RecaptchaFilter extends HttpFilter {
    @Inject
    private Models models;

    @Inject
    private RecaptchaKeysProvider recaptchaKeysProvider;

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            models.put("recaptchaSiteKey", recaptchaKeysProvider.getSiteKey());
            chain.doFilter(request, response);
        } catch (PropertiesLoadException e) {
            e.printStackTrace();
        }
    }

}