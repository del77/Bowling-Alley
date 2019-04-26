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
import pl.lodz.p.it.ssbd2019.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccountAccessLevel;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityRetrievalException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityUpdateException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AccountAccessLevelServiceImplTest {

    @Mock
    private AccountAccessLevelRepositoryLocal accountAccessLevelRepositoryLocal;

    @InjectMocks
    private AccountAccessLevelServiceImpl accountAccessLevelService;

    @Test
    public void shouldThrowEntityUpdateExceptionWhenUpdateAccessLevelCatchesException() {
        when(accountAccessLevelRepositoryLocal.edit(any(AccountAccessLevel.class))).thenThrow(RuntimeException.class);
        Assertions.assertThrows(EntityUpdateException.class, () -> accountAccessLevelService.updateAccountAccessLevels(0L,"test",true));
    }
}