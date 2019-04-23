package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository.AccessLevelRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository.AccountAccessLevelRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository.AccountRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository.UserRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccessLevel;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.User;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityRetrievalException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.RegistrationProcessException;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RegistrationServiceImplTest {

    @Mock
    private AccountRepositoryLocal accountRepositoryLocal;

    @Mock
    private UserRepositoryLocal userRepositoryLocal;

    @Mock
    private AccessLevelRepositoryLocal accessLevelRepositoryLocal;

    @Mock
    private AccountAccessLevelRepositoryLocal accountAccessLevelRepositoryLocal;

    @InjectMocks
    private RegistrationServiceImpl registrationService;

    @Test
    public void shouldThrowOnPasswordNull() {
        User user = mock(User.class);
        Account account = mock(Account.class);
        Assertions.assertThrows(
                RegistrationProcessException.class,
                () -> registrationService.registerAccount(account, user)
        );
    }

    @Test
    public void shouldThrowOnAccountNull() {
        User user = mock(User.class);
        Account account = null;
        Assertions.assertThrows(
                RegistrationProcessException.class,
                () -> registrationService.registerAccount(account, user)
        );
    }

    @Test
    public void shouldThrowOnUserNull() {
        User user = null;
        Account account = mock(Account.class);
        Assertions.assertThrows(
                RegistrationProcessException.class,
                () -> registrationService.registerAccount(account, user)
        );
    }

    @Test
    public void shouldAssignIdForUserAndAccount() throws RegistrationProcessException, EntityRetrievalException {
        User user = new User();
        Account account = Account
                .builder()
                .id(11L)
                .password("123")
                .build();
        when(accountRepositoryLocal.create(any(Account.class))).thenReturn(account);
        when(userRepositoryLocal.create(any(User.class))).thenReturn(user);
        when(accessLevelRepositoryLocal.findByName("CLIENT")).thenReturn(
            Optional.of(new AccessLevel())
        );
        registrationService.registerAccount(account, user);
        Assertions.assertEquals(account.getId(), user.getId());
    }

    @Test
    public void shouldThrowOnNoAccessLevel() {
        when(accessLevelRepositoryLocal.findByName("CLIENT")).thenReturn(Optional.empty());
        Account account = Account.builder().password("text").build();
        User user = new User();
        Assertions.assertThrows(
                EntityRetrievalException.class,
                () -> registrationService.registerAccount(account, user)
        );
    }

    @Test
    public void shouldThrowOnConfirmAccountWhenUserDoesNotExist() {
        when(accountRepositoryLocal.findById(any())).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityRetrievalException.class, () -> registrationService.confirmAccount(0));
    }

    @Test
    public void shouldSetFlagOnConfirmAccount() throws EntityRetrievalException {
        Account account = Account
                .builder()
                .confirmed(false)
                .build();
        when(accountRepositoryLocal.findById(any())).thenReturn(Optional.of(account));
        registrationService.confirmAccount(0);
        Assertions.assertTrue(account.isConfirmed());
    }
}
