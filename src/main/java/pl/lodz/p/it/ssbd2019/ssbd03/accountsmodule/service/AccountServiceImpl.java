package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service;

import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository.AccountRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.ChangePasswordException;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.SHA256Provider;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class AccountServiceImpl implements AccountService {
    @EJB(beanName = "MOKAccountRepository")
    AccountRepositoryLocal accountRepositoryLocal;

    @Override
    public Account changePassword(String login, String currentPassword, String newPassword) throws ChangePasswordException {
        try {
            Account account = this.findByLogin(login);
            String currentPasswordHash = SHA256Provider.encode(currentPassword);
            String newPasswordHash = SHA256Provider.encode(newPassword);

            if (!currentPasswordHash.equals(account.getPassword())) {
                throw new ChangePasswordException("Current password is incorrect.");
            }

            account.setPassword(newPasswordHash);
            return accountRepositoryLocal.edit(account);
        } catch (Exception e) {
            throw new ChangePasswordException(e.getMessage());
        }
    }

    @Override
    public Account findByLogin(String login) {
        // todo
        return accountRepositoryLocal.findById(4L).get();
    }
}
