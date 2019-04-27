package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository.AccessLevelRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository.UserAccountRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.dto.AccessVersionDto;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccessLevel;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccountAccessLevel;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityCreationException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityRetrievalException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityUpdateException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserAccountServiceImplTest {

    @Mock
    private UserAccountRepositoryLocal userAccountRepositoryLocal;

    @Mock
    AccessLevelRepositoryLocal accessLevelRepositoryLocal;

    @InjectMocks
    private UserAccountServiceImpl userService;

    @Test
    public void shouldThrowEntityRetrievalExceptionWhenGetAllUsersCatchesException() {
        when(userAccountRepositoryLocal.findAll()).thenThrow(RuntimeException.class);
        Assertions.assertThrows(EntityRetrievalException.class, () -> userService.getAllUsers());
    }

    @Test
    public void shouldThrowEntityRetrievalExceptionWhenGetUserByIdCatchesException() {
        when(userAccountRepositoryLocal.findById(any())).thenThrow(RuntimeException.class);
        Assertions.assertThrows(EntityRetrievalException.class, () -> userService.getUserById(1L));
    }

    @Test
    public void shouldThrowEntityCreationExceptionWhenAddUserCatchesException() {
        when(userAccountRepositoryLocal.create(any(UserAccount.class))).thenThrow(RuntimeException.class);
        Assertions.assertThrows(EntityCreationException.class, () -> userService.addUser(mock(UserAccount.class)));
    }



    @Test
    public void shouldReturnAllUsersOnGetAllUsers() throws EntityRetrievalException {
        List<UserAccount> allUserAccounts = Arrays.asList( mock(UserAccount.class), mock(UserAccount.class), mock(UserAccount.class) );
        when(userAccountRepositoryLocal.findAll()).thenReturn(allUserAccounts);
        Assertions.assertEquals( allUserAccounts.size(), userService.getAllUsers().size() );
    }

    @Test
    public void shouldReturnRightUserOnGetUserById() throws EntityRetrievalException {
        UserAccount userAccount = new UserAccount();
        when(userAccountRepositoryLocal.findById(1L)).then((u) -> {
            Long id = u.getArgument(0);
            userAccount.setId(id);
            return Optional.of(userAccount);
        });
        Assertions.assertEquals( userService.getUserById(1L), userAccount);
    }

    @Test
    public void shouldReturnRightEntityOnAddUser() throws  EntityCreationException {
        UserAccount userAccount = new UserAccount();
        when(userAccountRepositoryLocal.create(any(UserAccount.class))).then((u) -> {
            UserAccount newUserAccount = u.getArgument(0);
            newUserAccount.setId(1L);
            return newUserAccount;
        });
        Assertions.assertEquals( userService.addUser(userAccount).getId(), 1L);
    }

    @Test
    public void shouldThrowEntityUpdateExceptionWhenUpdateUserCatchesException() {
        when(userAccountRepositoryLocal.findById(anyLong())).thenThrow(RuntimeException.class);
        Assertions.assertThrows(EntityUpdateException.class, () -> userService.updateUser(mock(UserAccount.class), Arrays.asList(mock(AccessVersionDto.class))));
    }

    @Test
    public void shouldReturnRightEntityOnUpdateUser() throws EntityUpdateException {
        UserAccount userAccount = UserAccount.builder().id(1L).build();
        when(userAccountRepositoryLocal.findById(anyLong())).then((u) -> Optional.of(userAccount));
        when(userAccountRepositoryLocal.edit(any(UserAccount.class))).then((u) -> {
            UserAccount newUserAccount = u.getArgument(0);
            newUserAccount.setId(1L);
            return newUserAccount;
        });
        Assertions.assertEquals( userService.updateUser(userAccount, new ArrayList<>()).getId(), 1L);
    }

    @Test
    public void shouldAddAccessLevelToAccountWhenItDidNotExistBefore() throws EntityUpdateException {
        UserAccount userAccount = UserAccount.builder().id(1L).accountAccessLevels(new ArrayList<>()).build();
        when(userAccountRepositoryLocal.findById(anyLong())).then((u) -> Optional.of(UserAccount.builder().id(1L).accountAccessLevels(new ArrayList<>()).build()));
        when(accessLevelRepositoryLocal.findByName(anyString())).then((u) -> Optional.of(new AccessLevel()));
        when(userAccountRepositoryLocal.edit(any(UserAccount.class))).then((u) -> {
            UserAccount newUserAccount = u.getArgument(0);
            newUserAccount.setId(1L);
            List<AccountAccessLevel> aals = Arrays.asList(AccountAccessLevel.builder().active(true).build());
            newUserAccount.setAccountAccessLevels(aals);
            return newUserAccount;
        });
        AccessVersionDto avd = new AccessVersionDto("CLIENT", 0L, true);
        UserAccount edited = userService.updateUser(userAccount, Arrays.asList(avd));
        Assertions.assertEquals(edited.getAccountAccessLevels().size(),
                userAccount.getAccountAccessLevels().size() + 1);
    }


    @Test
    public void shouldMakeExistingAccessLevelActiveWhenItWasNotActiveBefore() throws EntityUpdateException {
        AccountAccessLevel existingAAL = AccountAccessLevel.builder().active(false).build();
        UserAccount userAccount = UserAccount.builder().id(1L).accountAccessLevels(Arrays.asList(existingAAL)).build();
        when(userAccountRepositoryLocal.findById(anyLong())).then((u) -> Optional.of( UserAccount.builder().id(1L).accountAccessLevels(new ArrayList<>()).build()));
        when(accessLevelRepositoryLocal.findByName(anyString())).then((u) -> Optional.of(new AccessLevel()));
        when(userAccountRepositoryLocal.edit(any(UserAccount.class))).then((u) -> {
            UserAccount newUserAccount = u.getArgument(0);
            newUserAccount.setId(1L);
            List<AccountAccessLevel> aals = Arrays.asList(AccountAccessLevel.builder().active(true).build());
            newUserAccount.setAccountAccessLevels(aals);
            return newUserAccount;
        });
        AccessVersionDto avd = new AccessVersionDto("CLIENT", 0L, true);
        UserAccount edited = userService.updateUser(userAccount, Arrays.asList(avd));
        Assertions.assertEquals(edited.getAccountAccessLevels().size(),
                userAccount.getAccountAccessLevels().size());
        Assertions.assertTrue(userAccount.getAccountAccessLevels().get(0).isActive() == false &&
                edited.getAccountAccessLevels().get(0).isActive() == true);
    }

    @Test
    public void shouldMakeExistingAccessLevelNotActiveWhenItWasActiveBefore() throws EntityUpdateException {
        AccountAccessLevel existingAAL = AccountAccessLevel.builder().active(true).build();
        UserAccount userAccount = UserAccount.builder().id(1L).accountAccessLevels(Arrays.asList(existingAAL)).build();
        when(userAccountRepositoryLocal.findById(anyLong())).then((u) -> Optional.of( UserAccount.builder().id(1L).accountAccessLevels(new ArrayList<>()).build()));
        when(accessLevelRepositoryLocal.findByName(anyString())).then((u) -> Optional.of(new AccessLevel()));
        when(userAccountRepositoryLocal.edit(any(UserAccount.class))).then((u) -> {
            UserAccount newUserAccount = u.getArgument(0);
            newUserAccount.setId(1L);
            List<AccountAccessLevel> aals = Arrays.asList(AccountAccessLevel.builder().active(false).build());
            newUserAccount.setAccountAccessLevels(aals);
            return newUserAccount;
        });
        AccessVersionDto avd = new AccessVersionDto("CLIENT", 0L, false);
        UserAccount edited = userService.updateUser(userAccount, Arrays.asList(avd));
        Assertions.assertEquals(edited.getAccountAccessLevels().size(),
                userAccount.getAccountAccessLevels().size());
        Assertions.assertTrue(userAccount.getAccountAccessLevels().get(0).isActive() == true &&
                edited.getAccountAccessLevels().get(0).isActive() == false);
    }
}
