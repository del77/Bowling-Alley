package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service;

import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository.ConfirmationTokenRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository.UserAccountRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.ConfirmationToken;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.conflict.AccountAlreadyConfirmedException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.DataAccessException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.generalized.ConfirmationTokenException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.EntityRetrievalException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.notfound.TokenNotFoundException;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.TokenUtils;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.localization.LocalizedMessageProvider;
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
     * @throws SsbdApplicationException w przypadku, gdy nie uda się znaleźć tokena, konto jest już aktywne, nie uda się
     *                                    dokonać edycji użytkownika, bądź gdy użytkownik nie istnieje.
     */
    @Override
    public void activateAccountByToken(String token) throws SsbdApplicationException {
        ConfirmationToken tokenEntity = confirmationTokenRepository
                .findByToken(token)
                .orElseThrow(() -> new TokenNotFoundException("Couldn't find token with provided value."));
        @NotNull UserAccount userAccount = tokenEntity.getUserAccount();
        if (userAccount.isAccountConfirmed()) {
            throw new AccountAlreadyConfirmedException("Account already confirmed.");
        }
        userAccount.setAccountConfirmed(true);
        confirmationTokenRepository.editWithoutMerge(tokenEntity);

        messenger.sendMessage(
                userAccount.getEmail(),
                localization.get("bowlingAlley") + " - " + localization.get("accountStatusChanged"),
                localization.get("accountConfirmed")
        );
    }

    /**
     * Metoda tworzy token dla użytkownika, a następnie wysyła wiadomość mail. Uwaga: w przypadku, gdy nie uda się
     * wysłać wiadomości metoda nadal zwraca sukces. Wysyłanie wiadomości jest asynchroniczne.
     *
     * @param userAccount konto użytkownika.
     * @throws SsbdApplicationException w przypadku gdy użytkownik nie istnieje, nie uda się utrwalić encji ConfirmationToken
     */
    @Override
    public void createNewTokenForAccount(UserAccount userAccount) throws SsbdApplicationException {
        ConfirmationToken confirmationToken = this.buildTokenForUser(userAccount);
        String url = getActivationUrl(confirmationToken.getToken());

        messenger.sendMessage(
                userAccount.getEmail(),
                localization.get("bowlingAlley") + " - " + localization.get("activateAccount"),
                url
        );
    }

    private ConfirmationToken buildTokenForUser(UserAccount userAccount) throws DataAccessException {
        ConfirmationToken confirmationToken = ConfirmationToken
                .builder()
                .userAccount(userAccount)
                .token(TokenUtils.generate())
                .build();
        return confirmationTokenRepository.create(confirmationToken);
    }

    private UserAccount retrieveUser(String userName) throws DataAccessException {
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
