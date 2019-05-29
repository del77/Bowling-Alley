package pl.lodz.p.it.ssbd2019.ssbd03.mok.service;

import pl.lodz.p.it.ssbd2019.ssbd03.mok.repository.ResetPasswordTokenRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.mok.repository.UserAccountRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.ResetPasswordToken;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.conflict.TokenExpiredException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.EmailDoesNotExistException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.notfound.TokenNotFoundException;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.SHA256Provider;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.TokenUtils;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.localization.LocalizedMessageProvider;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.messaging.Messenger;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.mvc.Models;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.sql.Timestamp;

@Stateless
public class ResetPasswordServiceImpl implements ResetPasswordService {
    @EJB(beanName = "MOKUserRepository")
    UserAccountRepositoryLocal userAccountRepositoryLocal;

    @EJB(beanName = "MOKResetPasswordTokenRepository")
    ResetPasswordTokenRepositoryLocal resetPasswordTokenRepositoryLocal;

    @EJB
    private Messenger messenger;

    @Inject
    private Models models;

    @Inject
    private LocalizedMessageProvider localization;

    @Context
    private HttpServletRequest httpServletRequest;

    @Override
    @PermitAll
    public ResetPasswordToken requestResetPassword(String email) throws SsbdApplicationException {
        UserAccount userAccount = getUserByEmail(email);
        String token = TokenUtils.generate();
        Timestamp validity = TokenUtils.getValidity(24);
        String url = getResetPasswordUrl(token);

        ResetPasswordToken resetPasswordToken = ResetPasswordToken
                .builder()
                .token(token)
                .userAccount(userAccount)
                .validity(validity)
                .build();

        resetPasswordTokenRepositoryLocal.create(resetPasswordToken);

        messenger.sendMessage(
                email,
                localization.get("bowlingAlley") + " - " + localization.get("resetPassword"),
                url
        );

        return resetPasswordToken;
    }

    @Override
    @PermitAll
    public UserAccount resetPassword(String token, String newPassword) throws SsbdApplicationException {
        ResetPasswordToken resetPasswordToken = getToken(token);
        UserAccount userAccount = resetPasswordToken.getUserAccount();
        String newPasswordHash = SHA256Provider.encode(newPassword);

        if (!TokenUtils.isValid(resetPasswordToken.getValidity())) {
            throw new TokenExpiredException("token_expired");
        }

        userAccount.setPassword(newPasswordHash);
        resetPasswordToken.setValidity(new Timestamp(System.currentTimeMillis() - 1));
        userAccountRepositoryLocal.editWithoutMerge(userAccount);

        return userAccount;
    }

    /**
     * Pobiera użytkownika o zadanym adresie email
     *
     * @param email Adres email powiązany z kontem
     * @return konto użytkownika
     * @throws EmailDoesNotExistException Wyjątek
     */
    private UserAccount getUserByEmail(String email) throws EmailDoesNotExistException {
        return userAccountRepositoryLocal.findByEmail(email).orElseThrow(EmailDoesNotExistException::new);
    }

    /**
     * Pobiera token dla zadanego ciągu
     *
     * @param token Unikalny token
     * @return token
     * @throws TokenNotFoundException w przypadku, gdy nie znajdzie zadanego tokena.
     */
    private ResetPasswordToken getToken(String token) throws TokenNotFoundException {
        return resetPasswordTokenRepositoryLocal.findByToken(token).orElseThrow(TokenNotFoundException::new);
    }

    private String getResetPasswordUrl(String token) {
        String fullAddress = this.httpServletRequest.getRequestURL().toString();
        String contextPath = models.get("webContextPath", String.class);

        return fullAddress.substring(0, fullAddress.indexOf(contextPath)) + models.get("webContextPath") + "/reset-password/" + token;
    }
}
