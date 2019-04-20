package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service;

import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository.AccessLevelRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository.AccountAccessLevelRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository.AccountRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository.UserRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccessLevel;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccountAccessLevel;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.User;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityRetrievelException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.RegistrationProcessException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.TextParsingException;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.SHA256Provider;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.swing.text.html.parser.Entity;
import javax.transaction.Transactional;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@RequestScoped
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
    public void registerAccount(Account account, User user) throws RegistrationProcessException, EntityRetrievelException {
        try {
            account.setPassword(
                    SHA256Provider.encode( account.getPassword() )
            );
        } catch (Exception e) {
            throw new RegistrationProcessException(e.getMessage());
        }
        Optional<AccessLevel> accessLevelOptional = accessLevelRepositoryLocal.findByName("CLIENT");
        AccessLevel accessLevel = accessLevelOptional
                .orElseThrow( () -> new EntityRetrievelException("Could not retrieve access level for CLIENT."));
        AccountAccessLevel accountAccessLevel = new AccountAccessLevel();
        Account persistedAccount = accountRepositoryLocal.create(account);
        accountAccessLevel.setAccount(persistedAccount);
        accountAccessLevel.setAccessLevel(accessLevel);
        accountAccessLevel.setActive(true);
        user.setId(persistedAccount.getId());
        user.setAccount(persistedAccount);
        userRepositoryLocal.create(user);
        accountAccessLevelRepositoryLocal.create(accountAccessLevel);
    }

    @Override
    public void confirmAccount(long accountId) throws EntityRetrievelException {
        //TODO: Token generation for this to be useful
        Optional<Account> accountOptional = accountRepositoryLocal.findById(accountId);
        Account account = accountOptional
                .orElseThrow( () -> new EntityRetrievelException("No Account with ID specified."));
        account.setConfirmed(true);
        accountRepositoryLocal.edit(account);
    }
}
