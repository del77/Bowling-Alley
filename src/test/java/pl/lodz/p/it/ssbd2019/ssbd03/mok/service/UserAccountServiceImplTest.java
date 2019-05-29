package pl.lodz.p.it.ssbd2019.ssbd03.mok.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.lodz.p.it.ssbd2019.ssbd03.mok.repository.AccessLevelRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.mok.repository.UserAccountRepositoryLocal;
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
import java.util.stream.Collectors;

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
    public void shouldThrowEntityRetrievalExceptionWhenGetByLoginCatchesException() throws SsbdApplicationException {
        when(userAccountRepositoryLocal.findByLogin(any())).thenThrow(EntityRetrievalException.class);
        Assertions.assertThrows(EntityRetrievalException.class, () -> userService.getByLogin("login"));
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

//    @Test
//    public void shouldReturnOneUser(){
//        UserAccount userAccount1 = UserAccount.builder()
//                .firstName("a")
//                .build();
//        UserAccount userAccount2 = UserAccount.builder()
//                .firstName("b")
//                .build();
//        List<UserAccount> list = new ArrayList<>();
//        list.add(userAccount1);
//        list.add(userAccount2);
//        String name = "a";
//        when(userAccountRepositoryLocal.findAllByNameOrLastName("a")).then((u) -> {
//            return list.stream().filter((UserAccount a) -> a.getFirstName().equals("a")).collect(Collectors.toList());
//        });
//        List<UserAccount> result = userAccountRepositoryLocal.findAllByNameOrLastName("a");
//        Assert.assertEquals(1,result.size());
//    }
//
//    @Test
//    public void updateUserAccountDetailsTest() throws SsbdApplicationException {
//        AccessLevel accessLevel = AccessLevel.builder()
//                .name("CLIENT")
//                .build();
//        AccountAccessLevel existingAccountAccessLevel = AccountAccessLevel.builder()
//                .active(true)
//                .accessLevel(accessLevel)
//                .build();
//        UserAccount userAccount = UserAccount.builder()
//                .id(1L)
//                .login("login")
//                .accountAccessLevels(new ArrayList<>(Collections.singletonList(existingAccountAccessLevel)))
//                .build();
//        when(userAccountRepositoryLocal.edit(any(UserAccount.class))).then((u) -> {
//            UserAccount edited = u.getArgument(0);
//            edited.setLogin(String.format("new %s", edited.getLogin()));
//            return edited;
//        });
//        try {
//            Assertions.assertEquals("new login", userService.updateUser(userAccount).getLogin());
//        } catch (EntityUpdateException e) {
//            Assertions.fail(e);
//        }
//    }
}
