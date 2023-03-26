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

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UpdateUserTest {
    @Mock
    UserRepository repository;
    @InjectMocks
    UserService service;
    User newUser, user;

    @Before
    public void setUp() {
        user = new User("jorge", 500);
        // Given
        newUser = new User("jorge", 1_000);
    }

    @Test
    public void givenUpdatedUser_whenUpdateUser_thenReturnUser() {
        when(repository.findUserByName("jorge")).thenReturn(Optional.ofNullable(user));
        when(repository.save(any(User.class))).thenReturn(newUser);

        // Given
        // SetUp

        // When
        User updatedUser = null;
        try {
            updatedUser = service.updateUser(newUser);
        } catch (UserServiceException e) {
            fail(e.getMessage());
        }

        // Then
        assertEquals(user.getName(), updatedUser.getName());
        assertNotEquals(user.getCoins(), updatedUser.getCoins());
        verify(repository, times(1)).save(newUser);
        verify(repository, times(1)).findUserByName("jorge");
    }

    @Test
    public void givenNonExistingUpdatedUser_whenUpdateUser_thenThrowsNotFoundUserServiceException() {
        when(repository.findUserByName("jorge")).thenReturn(Optional.empty());

        // Given
        // SetUp

        // When
        try {
            service.updateUser(newUser);
        } catch (UserServiceException e) {
            assertEquals(UserServiceException.NOT_FOUND, e.getMessage());
        }

        // Then
        verify(repository, times(0)).save(newUser);
        verify(repository, times(1)).findUserByName("jorge");
    }
}
