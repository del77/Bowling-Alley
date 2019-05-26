package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service;

import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository.AccessLevelRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository.UserAccountRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccessLevel;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccountAccessLevel;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.EntityRetrievalException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.UserIdDoesNotExistException;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.SHA256Provider;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.tracker.InterceptorTracker;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.tracker.TransactionTracker;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
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

    @EJB
    private ConfirmationTokenService confirmationTokenService;

    @EJB(beanName = "MOKAccessLevelRepository")
    private AccessLevelRepositoryLocal accessLevelRepositoryLocal;

    @Override
    public void registerAccount(UserAccount userAccount, List<String> accessLevelNames)
            throws SsbdApplicationException {
        userAccount.setPassword(encodePassword(userAccount.getPassword()));
        userAccount.setAccountAccessLevels(createAccountAccessLevels(userAccount, accessLevelNames));
        createUser(userAccount);
        confirmationTokenService.createNewTokenForAccount(
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
}
