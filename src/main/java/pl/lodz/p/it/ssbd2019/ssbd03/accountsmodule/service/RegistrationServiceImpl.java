package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service;

import org.hibernate.exception.ConstraintViolationException;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository.AccessLevelRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository.AccountAccessLevelRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository.UserAccountRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccessLevel;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccountAccessLevel;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityRetrievalException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityUpdateException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.NotUniqueParameterException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.RegistrationProcessException;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.SHA256Provider;

import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
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
    public void registerAccount(UserAccount userAccount, List<String> accessLevelNames) throws RegistrationProcessException, EntityRetrievalException, NotUniqueParameterException {
        userAccount.setPassword(encodePassword(userAccount.getPassword()));
        userAccount = createUser(userAccount);
        createAccountAccessLevels(userAccount, accessLevelNames);
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

    private UserAccount createUser(UserAccount userAccount) throws NotUniqueParameterException, RegistrationProcessException {
        try {
            return userAccountRepositoryLocal.create(userAccount);
        } catch (EJBTransactionRolledbackException e) {
            Throwable t = e.getCause();
            while ((t != null) && !(t instanceof ConstraintViolationException)) {
                t = t.getCause();
            }
            if (t instanceof ConstraintViolationException) {
                throw new NotUniqueParameterException();
            }
        }
        throw new RegistrationProcessException("Something went wrong during creation a user in database.");
    }

    private void createAccountAccessLevels(UserAccount userAccount, List<String> accessLevelNames) throws EntityRetrievalException {
        for (String accessLevelName : accessLevelNames) {
            createAccountAccessLevel(userAccount, accessLevelName);
        }
    }

    private void createAccountAccessLevel(UserAccount userAccount, String accessLevelName) throws EntityRetrievalException {
        Optional<AccessLevel> accessLevelOptional = accessLevelRepositoryLocal.findByName(accessLevelName);

        AccessLevel accessLevel = accessLevelOptional
                .orElseThrow(() -> new EntityRetrievalException(String.format("Could not retrieve access level for %s.", accessLevelName)));
        AccountAccessLevel accountAccessLevel = AccountAccessLevel
                .builder()
                .accessLevel(accessLevel)
                .account(userAccount)
                .active(true)
                .build();
        accountAccessLevelRepositoryLocal.create(accountAccessLevel);
    }
}
