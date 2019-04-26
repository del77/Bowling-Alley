package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository.UserAccountRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityCreationException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityRetrievalException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityUpdateException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserAccountServiceImplTest {

    @Mock
    private UserAccountRepositoryLocal userAccountRepositoryLocal;

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

//    @Test
//    public void shouldThrowEntityUpdateExceptionWhenUpdateUserCatchesException() {
//        when(userAccountRepositoryLocal.edit(any(UserAccount.class))).thenThrow(RuntimeException.class);
//        Assertions.assertThrows(EntityUpdateException.class, () -> userService.updateUser(mock(UserAccount.class), selectedAccessLevels));
//    }

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

//    @Test
//    public void shouldReturnRightEntityOnUpdateUser() throws EntityUpdateException {
//        UserAccount userAccount = new UserAccount();
//        when(userAccountRepositoryLocal.edit(any(UserAccount.class))).then((u) -> {
//            UserAccount newUserAccount = u.getArgument(0);
//            newUserAccount.setId(1L);
//            return newUserAccount;
//        });
//        Assertions.assertEquals( userService.updateUser(userAccount, selectedAccessLevels).getId(), 1L);
//    }
}
