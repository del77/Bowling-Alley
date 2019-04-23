package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service;

import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository.AccessLevelRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository.AccountAccessLevelRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository.AccountRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository.UserRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccessLevel;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccountAccessLevel;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.User;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityRetrievalException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.RegistrationProcessException;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.SHA256Provider;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.Optional;

@Stateless
@Transactional
public class RegistrationServiceImpl implements RegistrationService {
    @EJB(beanName = "MOKAccountRepository")
    AccountRepositoryLocal accountRepositoryLocal;
    @EJB(beanName = "MOKUserRepository")
    UserRepositoryLocal userRepositoryLocal;
    @EJB(beanName = "MOKAccountAccessLevelRepository")
    AccountAccessLevelRepositoryLocal accountAccessLevelRepositoryLocal;
    @EJB(beanName = "MOKAccessLevelRepository")
    AccessLevelRepositoryLocal accessLevelRepositoryLocal;

    @Override
    public void registerAccount(Account account, User user) throws RegistrationProcessException, EntityRetrievalException {
        try {
            account.setPassword(
                    SHA256Provider.encode( account.getPassword() )
            );
        } catch (Exception e) {
            throw new RegistrationProcessException(e.getMessage());
        }

        Optional<AccessLevel> accessLevelOptional = accessLevelRepositoryLocal.findByName("CLIENT");
        AccessLevel accessLevel = accessLevelOptional
                .orElseThrow( () -> new EntityRetrievalException("Could not retrieve access level for CLIENT."));

        Account persistedAccount = accountRepositoryLocal.create(account);

        user.setId(persistedAccount.getId());
        user.setAccount(persistedAccount);
        userRepositoryLocal.create(user);

        AccountAccessLevel accountAccessLevel = AccountAccessLevel
                .builder()
                .accessLevel(accessLevel)
                .account(persistedAccount)
                .active(true)
                .build();
        accountAccessLevelRepositoryLocal.create(accountAccessLevel);
    }

    @Override
    public void confirmAccount(long accountId) throws EntityRetrievalException {
        //TODO: Token generation for this to be useful
        Optional<Account> accountOptional = accountRepositoryLocal.findById(accountId);
        Account account = accountOptional
                .orElseThrow( () -> new EntityRetrievalException("No Account with ID specified."));
        account.setConfirmed(true);
        accountRepositoryLocal.edit(account);
    }
}
