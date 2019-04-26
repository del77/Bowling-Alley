package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service;

import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository.AccessLevelRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository.AccountAccessLevelRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository.AccountRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccessLevel;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccountAccessLevel;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityRetrievalException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityUpdateException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Stateless
@Transactional
public class AccountAccessLevelServiceImpl implements AccountAccessLevelService {

    @EJB(beanName = "MOKAccountAccessLevelRepository")
    AccountAccessLevelRepositoryLocal accountAccessLevelRepositoryLocal;

    @EJB(beanName = "MOKAccountRepository")
    AccountRepositoryLocal accountRepositoryLocal;

    @EJB(beanName = "MOKAccessLevelRepository")
    AccessLevelRepositoryLocal accessLevelRepositoryLocal;

    @Override
    public List<AccountAccessLevel> getAccountAccessLevelsByUserId(Long id) throws EntityRetrievalException {
        try {
            Optional<Account> accountOptional = accountRepositoryLocal.findById(id);
            Account account = accountOptional.orElseThrow( () -> new EntityRetrievalException("Could not retrieve Account for given id."));
            return accountAccessLevelRepositoryLocal.findAllForAccount(account);
        } catch (Exception e) {
            throw new EntityRetrievalException("Could not retrieve users list", e);
        }
    }

    @Override
    public void updateAccountAccessLevels(long accountId, String accessLevelName, boolean active) throws EntityUpdateException {
        try {
            Account account = accountRepositoryLocal.findById(accountId).orElseThrow(
                    () -> new EntityUpdateException("No such account with given id"));
            AccessLevel al = accessLevelRepositoryLocal.findByName(accessLevelName).orElseThrow(
                    () -> new EntityUpdateException("No such AccessLevel with given name"));
            AccountAccessLevel userAccountAccessLevel = accountAccessLevelRepositoryLocal.findForAccountIdAndAccessLevelId(account, al);

            if(userAccountAccessLevel != null) {
                userAccountAccessLevel.setActive(active);
                accountAccessLevelRepositoryLocal.edit(userAccountAccessLevel);
            }
            else {
                if(active) {
                    AccountAccessLevel newAccountAccessLevel = AccountAccessLevel.builder()
                            .accessLevel(al)
                            .account(account)
                            .active(true)
                            .build();
                    accountAccessLevelRepositoryLocal.create(newAccountAccessLevel);
                }
            }

        } catch (Exception e) {
            throw new EntityUpdateException("Could not update AccountAccessLevel", e);
        }
    }
}
