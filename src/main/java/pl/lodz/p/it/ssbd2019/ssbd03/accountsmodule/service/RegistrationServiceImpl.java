package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service;

import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository.AccessLevelRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository.AccountAccessLevelRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository.UserAccountRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccessLevel;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccountAccessLevel;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityRetrievalException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityUpdateException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.RegistrationProcessException;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.SHA256Provider;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
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
    public void registerAccount(UserAccount userAccount) throws RegistrationProcessException, EntityRetrievalException {
        try {
            userAccount.setPassword(
                    SHA256Provider.encode( userAccount.getPassword() )
            );
        } catch (Exception e) {
            throw new RegistrationProcessException(e.getMessage());
        }

        Optional<AccessLevel> accessLevelOptional = accessLevelRepositoryLocal.findByName("CLIENT");
        AccessLevel accessLevel = accessLevelOptional
                .orElseThrow( () -> new EntityRetrievalException("Could not retrieve access level for CLIENT."));

        userAccount = userAccountRepositoryLocal.create(userAccount);

        AccountAccessLevel accountAccessLevel = AccountAccessLevel
                .builder()
                .accessLevel(accessLevel)
                .account(userAccount)
                .active(true)
                .build();
        accountAccessLevelRepositoryLocal.create(accountAccessLevel);
    }

    @Override
    public void confirmAccount(long accountId) throws EntityRetrievalException, EntityUpdateException {
        //TODO: Token generation for this to be useful
        Optional<UserAccount> accountOptional = userAccountRepositoryLocal.findById(accountId);
        UserAccount account = accountOptional
                .orElseThrow( () -> new EntityRetrievalException("No Account with ID specified."));
        account.setAccountConfirmed(true);
        try {
            userAccountRepositoryLocal.edit(account);
        } catch (final Exception e) {
            throw new EntityUpdateException("Exception during update of user account (account confirmation)", e);
        }
    }
}
