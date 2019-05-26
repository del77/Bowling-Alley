package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository.AccessLevelRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository.UserAccountRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccessLevel;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccountAccessLevel;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.DataAccessException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.EntityRetrievalException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.EntityUpdateException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.generalized.ChangePasswordException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.LoginDoesNotExistException;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.SHA256Provider;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.localization.LocalizedMessageProvider;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.messaging.Messenger;

import java.util.*;

import static java.util.Arrays.asList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserAccountServiceImplTest {

    @Mock
    private UserAccountRepositoryLocal userAccountRepositoryLocal;

    @Mock
    private AccessLevelRepositoryLocal accessLevelRepositoryLocal;

    @Mock
    private Messenger messenger;

    @Mock
    private LocalizedMessageProvider localization;

    @InjectMocks
    private UserAccountServiceImpl userService;

    @Test
    public void shouldThrowEntityRetrievalExceptionWhenGetAllUsersCatchesException() throws DataAccessException {
        when(userAccountRepositoryLocal.findAll()).thenThrow(EntityRetrievalException.class);
        Assertions.assertThrows(EntityRetrievalException.class, () -> userService.getAllUsers());
    }

    @Test
    public void shouldThrowEntityRetrievalExceptionWhenGetUserByIdCatchesException() throws DataAccessException {
        when(userAccountRepositoryLocal.findById(any())).thenThrow(EntityRetrievalException.class);
        Assertions.assertThrows(EntityRetrievalException.class, () -> userService.getUserById(1L));
    }


    @Test
    public void shouldReturnAllUsersOnGetAllUsers() throws SsbdApplicationException {
        List<UserAccount> allUserAccounts = asList(mock(UserAccount.class), mock(UserAccount.class), mock(UserAccount.class));
        when(userAccountRepositoryLocal.findAll()).thenReturn(allUserAccounts);
        Assertions.assertEquals(allUserAccounts.size(), userService.getAllUsers().size());
    }

    @Test
    public void shouldReturnRightUserOnGetUserById() throws SsbdApplicationException {
        UserAccount userAccount = new UserAccount();
        when(userAccountRepositoryLocal.findById(1L)).then((u) -> {
            Long id = u.getArgument(0);
            userAccount.setId(id);
            return Optional.of(userAccount);
        });
        Assertions.assertEquals(userService.getUserById(1L), userAccount);
    }

    @Test
    public void shouldReturnRightUserOnGetByLogin() throws SsbdApplicationException {
        UserAccount userAccount = new UserAccount();
        when(userAccountRepositoryLocal.findByLogin("login")).then((u) -> {
            String login = u.getArgument(0);
            userAccount.setLogin(login);
            return Optional.of(userAccount);
        });
        Assertions.assertEquals(userService.getByLogin("login"), userAccount);
    }

    @Test
    public void shouldThrowEntityRetrievalExceptionWhenGetByLoginCatchesException() throws SsbdApplicationException {
        when(userAccountRepositoryLocal.findByLogin(any())).thenThrow(EntityRetrievalException.class);
        Assertions.assertThrows(EntityRetrievalException.class, () -> userService.getByLogin("login"));
    }

    @Test
    public void shouldReturnRightEntityOnUpdateUser() throws SsbdApplicationException {
        UserAccount userAccount = UserAccount.builder().id(1L).accountAccessLevels(new ArrayList<>()).build();
        when(userAccountRepositoryLocal.edit(any(UserAccount.class))).then((u) -> {
            UserAccount newUserAccount = u.getArgument(0);
            newUserAccount.setId(1L);
            return newUserAccount;
        });
        Assertions.assertEquals(userService.updateUser(userAccount).getId(), 1L);
    }



    @Test
    public void shouldReturnRightEntityOnUpdateUserRoles() throws SsbdApplicationException {
        UserAccount userAccount = UserAccount.builder().id(1L).accountAccessLevels(new ArrayList<>()).build();
        when(userAccountRepositoryLocal.edit(any(UserAccount.class))).then((u) -> {
            UserAccount newUserAccount = u.getArgument(0);
            newUserAccount.setId(1L);
            return newUserAccount;
        });
        Assertions.assertEquals(userService.updateUserAccessLevels(userAccount, new ArrayList<>()).getId(), 1L);
    }

    @Test
    public void shouldAddAccessLevelToAccountWhenItDidNotExistBefore() throws SsbdApplicationException {
        UserAccount userAccount = UserAccount.builder()
                .id(1L)
                .accountAccessLevels(new ArrayList<>())
                .build();
        AccessLevel accessLevel = AccessLevel.builder()
                .name("CLIENT")
                .build();
        int accessLevelsBefore = userAccount.getAccountAccessLevels().size();
        when(accessLevelRepositoryLocal.findByName("CLIENT")).thenReturn(Optional.of(accessLevel));
        when(userAccountRepositoryLocal.edit(any(UserAccount.class))).then((u) -> {
            UserAccount newUserAccount = u.getArgument(0);
            newUserAccount.setId(1L);
            List<AccountAccessLevel> accountAccessLevels = Collections.singletonList(AccountAccessLevel.builder()
                    .active(true)
                    .build());
            newUserAccount.setAccountAccessLevels(accountAccessLevels);
            return newUserAccount;
        });

        userAccount = userService.updateUserAccessLevels(userAccount, Collections.singletonList("CLIENT"));
        int accessLevelsAfter = userAccount.getAccountAccessLevels().size();
        Assertions.assertEquals(accessLevelsAfter, accessLevelsBefore + 1);
    }

    @Test
    public void unlockLockedAccountTestShouldNotThrow() throws SsbdApplicationException {
        UserAccount sut = UserAccount.builder().id(18L).accountActive(false).build();
        Optional<UserAccount> optionalAccount = Optional.of(sut);
        when(userAccountRepositoryLocal.findById(any(Long.class))).thenReturn(optionalAccount);
        when(userAccountRepositoryLocal.editWithoutMerge(any(UserAccount.class))).thenReturn(sut);
        try {
            Assertions.assertTrue(userService.updateLockStatusOnAccountById(18L, true).isAccountActive());
        } catch (EntityUpdateException e) {
            Assertions.fail(e);
        }
    }

    @Test
    public void unlockUnlockedAccountTestShouldNotThrow() throws SsbdApplicationException {
        UserAccount sut = UserAccount.builder().id(18L).accountActive(true).build();
        Optional<UserAccount> optionalAccount = Optional.of(sut);
        when(userAccountRepositoryLocal.findById(any(Long.class))).thenReturn(optionalAccount);
        when(userAccountRepositoryLocal.editWithoutMerge(any(UserAccount.class))).thenReturn(sut);
        try {
            Assertions.assertTrue(userService.updateLockStatusOnAccountById(18L, true).isAccountActive());
        } catch (EntityUpdateException e) {
            Assertions.fail(e);
        }
    }

    @Test
    public void lockLockedAccountTestShouldNotThrow() throws SsbdApplicationException {
        UserAccount sut = UserAccount.builder().id(18L).accountActive(false).build();
        Optional<UserAccount> optionalAccount = Optional.of(sut);
        when(userAccountRepositoryLocal.findById(any(Long.class))).thenReturn(optionalAccount);
        when(userAccountRepositoryLocal.editWithoutMerge(any(UserAccount.class))).thenReturn(sut);
        try {
            Assertions.assertFalse(userService.updateLockStatusOnAccountById(18L, false).isAccountActive());
        } catch (EntityUpdateException e) {
            Assertions.fail(e);
        }
    }

    @Test
    public void lockUnlockedAccountTestShouldNotThrow() throws SsbdApplicationException {
        UserAccount sut = UserAccount.builder().id(18L).accountActive(true).build();
        Optional<UserAccount> optionalAccount = Optional.of(sut);
        when(userAccountRepositoryLocal.findById(any(Long.class))).thenReturn(optionalAccount);
        when(userAccountRepositoryLocal.editWithoutMerge(any(UserAccount.class))).thenReturn(sut);
        try {
            Assertions.assertFalse(userService.updateLockStatusOnAccountById(18L, false).isAccountActive());
        } catch (EntityUpdateException e) {
            Assertions.fail(e);
        }
    }


    @Test
    public void shouldMakeExistingAccessLevelActiveWhenItWasNotActiveBefore() throws SsbdApplicationException {
        AccessLevel accessLevel = AccessLevel.builder()
                .name("CLIENT")
                .build();
        AccountAccessLevel existingAccountAccessLevel = AccountAccessLevel.builder()
                .active(false)
                .accessLevel(accessLevel)
                .build();
        UserAccount userAccount = UserAccount.builder()
                .id(1L)
                .accountAccessLevels(Collections.singletonList(existingAccountAccessLevel))
                .build();
        int accessLevelsBefore = userAccount.getAccountAccessLevels().size();
        boolean activeBefore = userAccount.getAccountAccessLevels().get(0).isActive();

        when(userAccountRepositoryLocal.edit(any(UserAccount.class))).then((u) -> {
            UserAccount newUserAccount = u.getArgument(0);
            newUserAccount.setId(1L);
            List<AccountAccessLevel> accountAccessLevels = Collections.singletonList(AccountAccessLevel.builder()
                    .active(true)
                    .build());
            newUserAccount.setAccountAccessLevels(accountAccessLevels);
            return newUserAccount;
        });

        userAccount = userService.updateUserAccessLevels(userAccount, new LinkedList<>(Collections.singletonList("CLIENT")));
        int accessLevelsAfter = userAccount.getAccountAccessLevels().size();
        boolean activeAfter = userAccount.getAccountAccessLevels().get(0).isActive();
        Assertions.assertEquals(accessLevelsAfter, accessLevelsBefore);
        Assertions.assertTrue(!activeBefore && activeAfter);
    }

    @Test
    public void shouldMakeExistingAccessLevelNotActiveWhenItWasActiveBefore() throws SsbdApplicationException {
        AccessLevel accessLevel = AccessLevel.builder()
                .name("CLIENT")
                .build();
        AccountAccessLevel existingAccountAccessLevel = AccountAccessLevel.builder()
                .active(true)
                .accessLevel(accessLevel)
                .build();
        UserAccount userAccount = UserAccount.builder()
                .id(1L)
                .accountAccessLevels(Collections.singletonList(existingAccountAccessLevel))
                .build();
        int accessLevelsBefore = userAccount.getAccountAccessLevels().size();
        boolean activeBefore = userAccount.getAccountAccessLevels().get(0).isActive();

        when(userAccountRepositoryLocal.edit(any(UserAccount.class))).then((u) -> {
            UserAccount newUserAccount = u.getArgument(0);
            newUserAccount.setId(1L);
            List<AccountAccessLevel> accountAccessLevels = Collections.singletonList(AccountAccessLevel.builder()
                    .active(false)
                    .build());
            newUserAccount.setAccountAccessLevels(accountAccessLevels);
            return newUserAccount;
        });

        userAccount = userService.updateUserAccessLevels(userAccount, new ArrayList<>());
        int accessLevelsAfter = userAccount.getAccountAccessLevels().size();
        boolean activeAfter = userAccount.getAccountAccessLevels().get(0).isActive();
        Assertions.assertEquals(accessLevelsAfter, accessLevelsBefore);
        Assertions.assertTrue(activeBefore && !activeAfter);
    }

    @Test
    public void changePasswordTestShouldNotThrow() {
        String login = "login69";
        String currentPassword = "test";
        String newPassword = "N0W3H45L0";

        try {
            String currentPasswordHash = SHA256Provider.encode(currentPassword);
            String newPasswordHash = SHA256Provider.encode(newPassword);

            UserAccount user = UserAccount
                    .builder()
                    .login(login)
                    .id(1L)
                    .password(currentPasswordHash)
                    .previousUserPasswords(new ArrayList<>())
                    .build();

            when(userAccountRepositoryLocal.findByLogin(any(String.class))).thenReturn(Optional.of(user));
            userService.changePasswordByLogin(login, currentPassword, newPassword);
            Assertions.assertEquals(newPasswordHash, user.getPassword());
        } catch (Exception e) {
            Assertions.fail(e);
        }
    }

    @Test
    public void changePasswordTestShouldThrowChangePasswordException() {
        String login = "login69";
        String currentPassword = "test";
        String wrongCurrentPassword = "T3ST";
        String newPassword = "N0W3H45L0";

        try {
            String currentPasswordHash = SHA256Provider.encode(currentPassword);

            UserAccount user = UserAccount
                    .builder()
                    .id(1L)
                    .login(login)
                    .password(currentPasswordHash)
                    .build();

            when(userAccountRepositoryLocal.findByLogin(any(String.class))).thenReturn(Optional.of(user));

            Assertions.assertThrows(ChangePasswordException.class, () ->
                    userService.changePasswordByLogin(login, wrongCurrentPassword, newPassword));
        } catch (Exception e) {
            Assertions.fail(e);
        }
    }

    @Test
    public void shouldChangeUserSPassword() {
        String newPassword = "password2";
        try {
            String currentPasswordHash = SHA256Provider.encode("password");
            String newPasswordHash = SHA256Provider.encode(newPassword);

            UserAccount userAccount = UserAccount.builder()
                    .password(currentPasswordHash)
                    .previousUserPasswords(new ArrayList<>())
                    .build();
            when(userAccountRepositoryLocal.findById(1L)).then((u) -> {
                Long id = u.getArgument(0);
                userAccount.setId(id);
                return Optional.of(userAccount);
            });

            userService.changePasswordById(1L, newPassword);
            Assertions.assertEquals(userAccount.getPassword(), newPasswordHash);
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @Test
    public void updateUserAccountDetailsTest() throws SsbdApplicationException {
        AccessLevel accessLevel = AccessLevel.builder()
                .name("CLIENT")
                .build();
        AccountAccessLevel existingAccountAccessLevel = AccountAccessLevel.builder()
                .active(true)
                .accessLevel(accessLevel)
                .build();
        UserAccount userAccount = UserAccount.builder()
                .id(1L)
                .login("login")
                .accountAccessLevels(new ArrayList<>(Collections.singletonList(existingAccountAccessLevel)))
                .build();
        when(userAccountRepositoryLocal.edit(any(UserAccount.class))).then((u) -> {
            UserAccount edited = u.getArgument(0);
            edited.setLogin(String.format("new %s", edited.getLogin()));
            return edited;
        });
        try {
            Assertions.assertEquals("new login", userService.updateUser(userAccount).getLogin());
        } catch (EntityUpdateException e) {
            Assertions.fail(e);
        }
    }
}
