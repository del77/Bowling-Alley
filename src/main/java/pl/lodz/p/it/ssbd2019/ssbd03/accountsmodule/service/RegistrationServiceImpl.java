package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service;

import org.postgresql.util.PSQLException;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository.AccessLevelRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository.AccountAccessLevelRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository.UserAccountRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccessLevel;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccountAccessLevel;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.*;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.SHA256Provider;

import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Stateless
@Transactional
public class RegistrationServiceImpl implements RegistrationService {
    @EJB(beanName = "MOKUserRepository")
    UserAccountRepositoryLocal userAccountRepositoryLocal;
    @EJB(beanName = "MOKAccountAccessLevelRepository")
    AccountAccessLevelRepositoryLocal accountAccessLevelRepositoryLocal;
    @EJB(beanName = "MOKAccessLevelRepository")
    AccessLevelRepositoryLocal accessLevelRepositoryLocal;

    @Override
    public void registerAccount(UserAccount userAccount, List<String> accessLevelNames) throws RegistrationProcessException, EntityRetrievalException, NotUniqueLoginException, NotUniqueEmailException {
        userAccount.setPassword(encodePassword(userAccount.getPassword()));
        userAccount = createUser(userAccount);
        userAccount.setAccountAccessLevels(createAccountAccessLevels(userAccount, accessLevelNames));
    }

    @Override
    public void confirmAccount(long accountId) throws EntityRetrievalException, EntityUpdateException {
        Optional<UserAccount> accountOptional = userAccountRepositoryLocal.findById(accountId);
        UserAccount account = accountOptional
                .orElseThrow(() -> new EntityRetrievalException("No Account with ID specified."));
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
            handleException(e);
        }
        throw new RegistrationProcessException("Something went wrong during creation a user in database.");
    }

    private void handleException(EJBTransactionRolledbackException e) throws NotUniqueLoginException, NotUniqueEmailException, RegistrationProcessException {
        Throwable t = e.getCause();
        while ((t != null) && !(t instanceof PSQLException)) {
            t = t.getCause();
            if (t == null || t.getMessage() == null) {
                throw new RegistrationProcessException(e.getMessage(), e);
            } else if (t.getMessage().contains("login")){
                throw new NotUniqueLoginException();
            } else if (t.getMessage().contains("email")) {
                throw new NotUniqueEmailException();
            }
        }
        throw new RegistrationProcessException(e.getMessage());
    }

    private List<AccountAccessLevel> createAccountAccessLevels(UserAccount userAccount, List<String> accessLevelNames) throws EntityRetrievalException {
        List<AccountAccessLevel> accessLevels = new ArrayList<>();
        for (String accessLevelName : accessLevelNames) {
            accessLevels.add(createAccountAccessLevel(userAccount, accessLevelName));
        }
        return accessLevels;
    }

    private AccountAccessLevel createAccountAccessLevel(UserAccount userAccount, String accessLevelName) throws EntityRetrievalException {
        Optional<AccessLevel> accessLevelOptional = accessLevelRepositoryLocal.findByName(accessLevelName);

        AccessLevel accessLevel = accessLevelOptional
                .orElseThrow(() -> new EntityRetrievalException(String.format("Could not retrieve access level for %s.", accessLevelName)));
        AccountAccessLevel accountAccessLevel = AccountAccessLevel
                .builder()
                .accessLevel(accessLevel)
                .account(userAccount)
                .active(true)
                .version(0L)
                .build();
        return accountAccessLevelRepositoryLocal.create(accountAccessLevel);
    }
}
