package pl.lodz.p.it.ssbd2019.ssbd03.web.jacc;


import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.AuthenticationException;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.authentication.mechanism.http.*;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStoreHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.glassfish.soteria.Utils;
import org.glassfish.soteria.mechanisms.FormAuthenticationMechanism;
import pl.lodz.p.it.ssbd2019.ssbd03.mok.service.UserAccountService;

/**
 * Klasa odpowiadająca za uwierzytelnienie w aplikacji
 */
@ApplicationScoped
public class SsbdAuthenticationMechanism extends FormAuthenticationMechanism {
    
    @EJB
    private UserAccountService userAccountService;
    
    @Inject
    IdentityStoreHandler identityStoreHandler;
    
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
        
        CredentialValidationResult result = identityStoreHandler.validate(
                new UsernamePasswordCredential(login, password));
    
        if (result.getStatus().equals(CredentialValidationResult.Status.VALID)) {
            try {
                userAccountService.restartFailedLoginsCounter(login);
                response.sendRedirect(request.getContextPath());
                return httpMessageContext.notifyContainerAboutLogin(result.getCallerPrincipal(), result.getCallerGroups());
            } catch (Exception e) {
                e.printStackTrace();
                return AuthenticationStatus.NOT_DONE;
            }
        } else {
            try {
                userAccountService.incrementFailedLoginsCounter(login);
                response.sendRedirect(String.format("%s/login/error", request.getContextPath()));
            } catch (Exception e) {
                e.printStackTrace();
                return AuthenticationStatus.NOT_DONE;
            }
            return AuthenticationStatus.SEND_FAILURE;
        }
    }
    
    private static boolean isValidFormPost(HttpServletRequest request) {
        return "POST".equals(request.getMethod()) && request.getRequestURI().endsWith("/j_security_check") && Utils.notNull(request.getParameter("j_username"), request.getParameter("j_password"));
    }
}