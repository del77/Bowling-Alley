package pl.lodz.p.it.ssbd2019.ssbd03.web.jacc;


import org.glassfish.soteria.Utils;
import org.glassfish.soteria.mechanisms.LoginToContinueHolder;
import pl.lodz.p.it.ssbd2019.ssbd03.mok.service.UserAccountService;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.AuthenticationException;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.authentication.mechanism.http.AutoApplySession;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.security.enterprise.authentication.mechanism.http.LoginToContinue;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStoreHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Klasa odpowiadająca za uwierzytelnienie w aplikacji
 */
@AutoApplySession
@LoginToContinue(errorPage = "/login/error")
@ApplicationScoped
public class SsbdAuthenticationMechanism implements HttpAuthenticationMechanism, LoginToContinueHolder {

    private static final String ERROR_PAGE = "/login/error";
    private static final Logger logger = Logger.getLogger(SsbdAuthenticationMechanism.class.getName());

    @EJB
    private UserAccountService userAccountService;

    @Inject
    IdentityStoreHandler identityStoreHandler;

    @Override
    public LoginToContinue getLoginToContinue() {
        return this.getClass().getAnnotation(LoginToContinue.class);
    }

    @Override
    public AuthenticationStatus validateRequest(
            HttpServletRequest request,
            HttpServletResponse response,
            HttpMessageContext httpMessageContext) throws AuthenticationException {

        if (!isValidFormPost(request)) {
            return AuthenticationStatus.NOT_DONE;
        }

        String login = request.getParameter("j_username");
        String password = request.getParameter("j_password");
        CredentialValidationResult credentialValidationResult = identityStoreHandler.validate(
                new UsernamePasswordCredential(login, password));

        try {
            if (credentialValidationResult.getStatus().equals(CredentialValidationResult.Status.VALID)) {
                AuthenticationStatus status =
                        httpMessageContext.notifyContainerAboutLogin(credentialValidationResult);
                userAccountService.restartFailedLoginsCounter(login);
                userAccountService.registerSuccessfulLoginDate(login);
                if(isRedirectedFromLoginPage(request)) {
                    response.sendRedirect(request.getContextPath());
                }
                return status;
            } else {
                logger.log(Level.WARNING, () -> "Failed attempt to authenticate for user login: " + login);
                userAccountService.incrementFailedLoginsCounter(login);
                userAccountService.registerFailedLoginDate(login);
                response.sendRedirect(String.format("%s%s", request.getContextPath(), ERROR_PAGE));
                return AuthenticationStatus.SEND_FAILURE;
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            try {
                response.sendRedirect(String.format("%s%s", request.getContextPath(), ERROR_PAGE));
            } catch (IOException io) {
                logger.log(Level.SEVERE, io.getMessage(), io);
            }
            return AuthenticationStatus.NOT_DONE;
        }
    }

    @Override
    public void cleanSubject(HttpServletRequest request, HttpServletResponse response, HttpMessageContext httpMessageContext) {
        HttpAuthenticationMechanism.super.cleanSubject(request, response, httpMessageContext);
    }

    private static boolean isValidFormPost(HttpServletRequest request) {
        return "POST".equals(request.getMethod())
                && request.getRequestURI().endsWith("/j_security_check")
                && Utils.notNull(request.getParameter("j_username"), request.getParameter("j_password"));
    }

    private boolean isRedirectedFromLoginPage(HttpServletRequest request) {
        return request.getHeader("referer").equals(request.getRequestURL().toString().replace(request.getRequestURI(), "") + request.getContextPath() + getLoginToContinue().loginPage());
    }
}