package org.jucajo.bj_on.services.userService;

import org.jucajo.bj_on.models.User;
import org.jucajo.bj_on.persistence.UserServiceException;
import org.jucajo.bj_on.repositories.UserRepository;
import org.jucajo.bj_on.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CreateUserTest {
    @Mock
    UserRepository repository;
    @InjectMocks
    UserService service;

    User user;

    @Before
    public void setUp() {
        // Given
        user = new User("camilo", 500);
    }

    @Test
    public void givenNewUser_whenCreateUser_thenReturnNewUser() {
        when(repository.findUserByName("camilo")).thenReturn(Optional.empty());
        when(repository.save(any(User.class))).thenReturn(user);

        // Given
        // SetUp

        //When
        User createdUser = null;
        try {
            createdUser = service.createUser(user);
        } catch (UserServiceException e) {
            fail(e.getMessage());
        }

        // Then
        assertEquals(user.getName(), createdUser.getName());
        assertEquals(user.getCoins(), createdUser.getCoins());
        verify(repository, times(1)).findUserByName("camilo");
        verify(repository, times(1)).save(user);
    }

    @Test
    public void givenExistingUser_whenCreateUser_thenThrowsAlreadyExistsUserServiceException() {
        when(repository.findUserByName("camilo")).thenReturn(Optional.ofNullable(user));

        // Given - When
        try {
            service.createUser(user);
        } catch (UserServiceException e) {
            // Then
            assertEquals(UserServiceException.ALREADY_EXISTS, e.getMessage());
        }

        verify(repository, times(1)).findUserByName("camilo");
        verify(repository, times(0)).save(user);
    }
}
