package pl.lodz.p.it.ssbd2019.ssbd03.mok.service;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.EntityRetrievalException;
import pl.lodz.p.it.ssbd2019.ssbd03.mok.repository.AccessLevelRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.mok.repository.UserAccountRepositoryLocal;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RegistrationServiceImplTest {

    @Mock
    private UserAccountServiceImpl userAccountService;

    @Mock
    private UserAccountRepositoryLocal userAccountRepositoryLocal;

    @Mock
    private AccessLevelRepositoryLocal accessLevelRepositoryLocal;

    @InjectMocks
    private RegistrationServiceImpl registrationService;

    @Test
    public void shouldThrowOnConfirmAccountWhenUserDoesNotExist() throws SsbdApplicationException {
        when(userAccountRepositoryLocal.findById(any())).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityRetrievalException.class, () -> registrationService.confirmAccount(0));
    }

    @Test
    public void shouldSetFlagOnConfirmAccount() throws SsbdApplicationException {
        UserAccount account = UserAccount
                .builder()
                .accountConfirmed(false)
                .build();
        when(userAccountRepositoryLocal.findById(any())).thenReturn(Optional.of(account));
        registrationService.confirmAccount(0);
        Assertions.assertTrue(account.isAccountConfirmed());
    }
}
