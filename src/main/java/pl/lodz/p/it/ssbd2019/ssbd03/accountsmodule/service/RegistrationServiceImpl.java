package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service;

import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository.AccessLevelRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository.UserAccountRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccessLevel;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccountAccessLevel;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.conflict.validation.NotUniqueEmailException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.conflict.validation.NotUniqueLoginException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.EntityRetrievalException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.EntityUpdateException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.generalized.RegistrationProcessException;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.UniqueConstraintViolationHandler;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.SHA256Provider;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.tracker.InterceptorTracker;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.tracker.TransactionTracker;

import javax.annotation.security.PermitAll;
import javax.ejb.*;
import javax.interceptor.Interceptors;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@PermitAll
@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Interceptors(InterceptorTracker.class)
public class RegistrationServiceImpl extends TransactionTracker implements RegistrationService {

    @EJB(beanName = "MOKUserRepository")
    private UserAccountRepositoryLocal userAccountRepositoryLocal;

    @EJB(beanName = "MOKAccessLevelRepository")
    private AccessLevelRepositoryLocal accessLevelRepositoryLocal;

    @Override
    public void registerAccount(UserAccount userAccount, List<String> accessLevelNames)
            throws RegistrationProcessException,
            EntityRetrievalException,
            NotUniqueLoginException,
            NotUniqueEmailException {
        userAccount.setPassword(encodePassword(userAccount.getPassword()));
        userAccount.setAccountAccessLevels(createAccountAccessLevels(userAccount, accessLevelNames));
        createUser(userAccount);
    }

    @Override
    public void confirmAccount(long accountId) throws EntityRetrievalException, EntityUpdateException {
        Optional<UserAccount> accountOptional = userAccountRepositoryLocal.findById(accountId);
        UserAccount account = accountOptional
                .orElseThrow(() -> new EntityRetrievalException("No Account with specified ID."));
        account.setAccountConfirmed(true);

        try {
            userAccountRepositoryLocal.edit(account);
        } catch (final Exception e) {
            throw new EntityUpdateException("Exception during update of user account (account confirmation)", e);
        }
    }

    private String encodePassword(String password) throws RegistrationProcessException {
        try {
            return SHA256Provider.encode(password);
        } catch (Exception e) {
            throw new RegistrationProcessException(e.getMessage());
        }
    }

    private UserAccount createUser(UserAccount userAccount) throws NotUniqueLoginException, RegistrationProcessException, NotUniqueEmailException {
        try {
            return userAccountRepositoryLocal.create(userAccount);
        } catch (EJBTransactionRolledbackException e) {
            UniqueConstraintViolationHandler.handleNotUniqueLoginOrEmailException(e, RegistrationProcessException.class);
        }
        throw new RegistrationProcessException("Something went wrong during creation a user in database.");
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
