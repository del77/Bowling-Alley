package pl.lodz.p.it.ssbd2019.ssbd03.mok.service;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccessLevel;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccountAccessLevel;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.ConfirmationToken;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.DataAccessException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.EntityRetrievalException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.LoginDoesNotExistException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.UserIdDoesNotExistException;
import pl.lodz.p.it.ssbd2019.ssbd03.mok.repository.AccessLevelRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.mok.repository.ConfirmationTokenRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.mok.repository.UserAccountRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.SHA256Provider;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.TokenUtils;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.localization.LocalizedMessageProvider;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.messaging.Messenger;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.tracker.InterceptorTracker;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.tracker.TransactionTracker;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.mvc.Models;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@PermitAll
@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors(InterceptorTracker.class)
public class RegistrationServiceImpl extends TransactionTracker implements RegistrationService {

    @EJB(beanName = "MOKUserRepository")
    private UserAccountRepositoryLocal userAccountRepositoryLocal;

    @EJB(beanName = "MOKAccessLevelRepository")
    private AccessLevelRepositoryLocal accessLevelRepositoryLocal;

    @EJB(beanName = "MOKConfirmationTokenRepository")
    private ConfirmationTokenRepositoryLocal confirmationTokenRepository;

    @Inject
    private LocalizedMessageProvider localization;

    @Inject
    private Models models;

    @Context
    private HttpServletRequest httpServletRequest;

    @EJB
    private Messenger messenger;

    @Override
    public void registerAccount(UserAccount userAccount, List<String> accessLevelNames)
            throws SsbdApplicationException {
        userAccount.setPassword(encodePassword(userAccount.getPassword()));
        userAccount.setAccountAccessLevels(createAccountAccessLevels(userAccount, accessLevelNames));
        createUser(userAccount);
        createNewTokenForAccount(
                userAccount);
    }

    @Override
    public void confirmAccount(long accountId) throws SsbdApplicationException {
        Optional<UserAccount> accountOptional = userAccountRepositoryLocal.findById(accountId);
        UserAccount account = accountOptional
                .orElseThrow(() -> new UserIdDoesNotExistException("Account with id '" + accountId + "' does not exist."));
        account.setAccountConfirmed(true);

        userAccountRepositoryLocal.editWithoutMerge(account);
    }

    private String encodePassword(String password) throws SsbdApplicationException {
        return SHA256Provider.encode(password);
    }

    private UserAccount createUser(UserAccount userAccount) throws SsbdApplicationException {
        return userAccountRepositoryLocal.create(userAccount);
    }


    private List<AccountAccessLevel> createAccountAccessLevels(UserAccount userAccount, List<String> accessLevelNames) throws EntityRetrievalException {
        List<AccountAccessLevel> accountAccessLevels = new ArrayList<>();
        for (String accessLevelName : accessLevelNames) {
            accountAccessLevels.add(createAccountAccessLevel(userAccount, accessLevelName));
        }
        return accountAccessLevels;
    }

    private AccountAccessLevel createAccountAccessLevel(UserAccount userAccount, String accessLevelName) throws EntityRetrievalException {
        Optional<AccessLevel> accessLevelOptional = accessLevelRepositoryLocal.findByName(accessLevelName);

        AccessLevel accessLevel = accessLevelOptional
                .orElseThrow(() -> new EntityRetrievalException(String.format("Could not retrieve access level for %s.", accessLevelName)));

        return AccountAccessLevel
                .builder()
                .accessLevel(accessLevel)
                .account(userAccount)
                .active(true)
                .version(0L)
                .build();
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
                .orElseThrow(() -> new LoginDoesNotExistException("No user account with that login"));
    }

    private String getActivationUrl(String token) {
        String fullAddress = this.httpServletRequest.getRequestURL().toString();
        String contextPath = models.get("webContextPath", String.class);

        return fullAddress.substring(0, fullAddress.indexOf(contextPath)) + models.get("webContextPath") + "/confirm-account/" + token;
    }
}
