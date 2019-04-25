package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository.UserRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.User;
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
public class UserServiceImplTest {

    @Mock
    private UserRepositoryLocal userRepositoryLocal;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void shouldThrowEntityRetrievalExceptionWhenGetAllUsersCatchesException() {
        when(userRepositoryLocal.findAll()).thenThrow(RuntimeException.class);
        Assertions.assertThrows(EntityRetrievalException.class, () -> userService.getAllUsers());
    }

    @Test
    public void shouldThrowEntityRetrievalExceptionWhenGetUserByIdCatchesException() {
        when(userRepositoryLocal.findById(any())).thenThrow(RuntimeException.class);
        Assertions.assertThrows(EntityRetrievalException.class, () -> userService.getUserById(1L));
    }

    @Test
    public void shouldThrowEntityCreationExceptionWhenAddUserCatchesException() {
        when(userRepositoryLocal.create(any(User.class))).thenThrow(RuntimeException.class);
        Assertions.assertThrows(EntityCreationException.class, () -> userService.addUser(mock(User.class)));
    }

//    @Test
//    public void shouldThrowEntityUpdateExceptionWhenUpdateUserCatchesException() {
//        when(userRepositoryLocal.edit(any(User.class))).thenThrow(RuntimeException.class);
//        Assertions.assertThrows(EntityUpdateException.class, () -> userService.updateUser(mock(User.class), null));
//    }

    @Test
    public void shouldReturnAllUsersOnGetAllUsers() throws EntityRetrievalException {
        List<User> allUsers = Arrays.asList( mock(User.class), mock(User.class), mock(User.class) );
        when(userRepositoryLocal.findAll()).thenReturn(allUsers);
        Assertions.assertEquals( allUsers.size(), userService.getAllUsers().size() );
    }

    @Test
    public void shouldReturnRightUserOnGetUserById() throws EntityRetrievalException {
        User user = new User();
        when(userRepositoryLocal.findById(1L)).then((u) -> {
            Long id = u.getArgument(0);
            user.setId(id);
            return Optional.of(user);
        });
        Assertions.assertEquals( userService.getUserById(1L), user);
    }

    @Test
    public void shouldReturnRightEntityOnAddUser() throws  EntityCreationException {
        User user = new User();
        when(userRepositoryLocal.create(any(User.class))).then((u) -> {
            User newUser = u.getArgument(0);
            newUser.setId(1L);
            return newUser;
        });
        Assertions.assertEquals( userService.addUser(user).getId(), 1L);
    }

//    @Test
//    public void shouldReturnRightEntityOnUpdateUser() throws EntityUpdateException {
//        User user = new User();
//        when(userRepositoryLocal.edit(any(User.class))).then((u) -> {
//            User newUser = u.getArgument(0);
//            newUser.setId(1L);
//            return newUser;
//        });
//        Assertions.assertEquals( userService.updateUser(user, null).getId(), 1L);
//    }

}
