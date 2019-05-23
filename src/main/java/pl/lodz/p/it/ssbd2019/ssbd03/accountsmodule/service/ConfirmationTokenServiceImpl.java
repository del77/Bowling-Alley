package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service;

import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository.ConfirmationTokenRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository.UserAccountRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.ConfirmationToken;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.AccountAlreadyConfirmedException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.ConfirmationTokenException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityRetrievalException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.TokenNotFoundException;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.TokenUtils;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.localization.LocalizedMessageProvider;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.messaging.ClassicMessage;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.messaging.Messenger;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.tracker.InterceptorTracker;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.tracker.TransactionTracker;

import javax.annotation.security.PermitAll;
import javax.ejb.*;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.mvc.Models;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.Context;
import java.util.Optional;

@PermitAll
@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Interceptors(InterceptorTracker.class)
public class ConfirmationTokenServiceImpl extends TransactionTracker implements ConfirmationTokenService {

    @EJB(beanName = "MOKConfirmationTokenRepository")
    private ConfirmationTokenRepositoryLocal confirmationTokenRepository;

    @EJB(beanName = "MOKUserRepository")
    private UserAccountRepositoryLocal userAccountRepositoryLocal;

    @EJB
    private Messenger messenger;

    @Inject
    private Models models;

    @Inject
    private LocalizedMessageProvider localization;

    @Context
    private HttpServletRequest httpServletRequest;

    /**
     * Metoda służy do aktywacji użytkownika na podstawie tokenu z nim powiązanego.
     *
     * @param token wartość tokena potwierdzenia
     * @throws ConfirmationTokenException w przypadku, gdy nie uda się znaleźć tokena, konto jest już aktywne, nie uda się
     *                                    dokonać edycji użytkownika, bądź gdy użytkownik nie istnieje.
     */
    @Override
    public void activateAccountByToken(String token) throws ConfirmationTokenException {
        try {
            ConfirmationToken tokenEntity = confirmationTokenRepository
                    .findByToken(token)
                    .orElseThrow(() -> new TokenNotFoundException("Couldn't find token with provided value."));
            @NotNull UserAccount userAccount = tokenEntity.getUserAccount();
            if (userAccount.isAccountConfirmed()) {
                throw new AccountAlreadyConfirmedException("Account already confirmed.");
            }
            userAccount.setAccountConfirmed(true);
            confirmationTokenRepository.edit(tokenEntity);

            messenger.sendMessage(
                    userAccount.getEmail(),
                    localization.get("bowlingAlley") + " - " + localization.get("accountStatusChanged"),
                    localization.get("accountConfirmed")
            );

        } catch (final Exception e) {
            throw new ConfirmationTokenException("Exception during token retrieval", e);
        }
    }

    /**
     * Metoda tworzy token dla użytkownika, a następnie wysyła wiadomość mail. Uwaga: w przypadku, gdy nie uda się
     * wysłać wiadomości metoda nadal zwraca sukces. Wysyłanie wiadomości jest asynchroniczne.
     *
     * @param userName nazwa użytkownika.
     * @throws ConfirmationTokenException w przypadku gdy użytkownik nie istnieje, nie uda się utrwalić encji ConfirmationToken
     */
    @Override
    public void createNewTokenForAccount(String userName) throws ConfirmationTokenException {
        try {
            @NotNull UserAccount userAccount = this.retrieveUser(userName);
            ConfirmationToken confirmationToken = this.buildTokenForUser(userAccount);
            String url = getActivationUrl(confirmationToken.getToken());

            messenger.sendMessage(
                    userAccount.getEmail(),
                    localization.get("bowlingAlley") + " - " + localization.get("activateAccount"),
                    url
            );
        } catch (final Exception e) {
            throw new ConfirmationTokenException("Could not create token.");
        }
    }

    private ConfirmationToken buildTokenForUser(UserAccount userAccount) {
        ConfirmationToken confirmationToken = ConfirmationToken
                .builder()
                .userAccount(userAccount)
                .token(TokenUtils.generate())
                .build();
        return confirmationTokenRepository.create(confirmationToken);
    }

    private UserAccount retrieveUser(String userName) throws EntityRetrievalException {
        Optional<UserAccount> userAccount =
                userAccountRepositoryLocal.findByLogin(userName);
        return userAccount
                .orElseThrow(() -> new EntityRetrievalException("Not user account with that login"));
    }

    private String getActivationUrl(String token) {
        String fullAddress = this.httpServletRequest.getRequestURL().toString();
        String contextPath = models.get("webContextPath", String.class);

        return fullAddress.substring(0, fullAddress.indexOf(contextPath)) + models.get("webContextPath") + "/confirm-account/" + token;
    }
}