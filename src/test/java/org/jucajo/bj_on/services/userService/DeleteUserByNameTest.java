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
public class DeleteUserByNameTest {
    @Mock
    UserRepository repository;
    @InjectMocks
    UserService service;
    User userDeleted;

    @Before
    public void setUp() {
        userDeleted = new User("juliana", 9_999_010);
    }

    @Test
    public void givenUserName_whenDeleteUserByName_thenReturnUser() {
        when(repository.findUserByName("juliana")).thenReturn(Optional.ofNullable(userDeleted));
        when(repository.deleteUserByName("juliana")).thenReturn(userDeleted);

        // Given
        String userName = "juliana";

        // When
        User user = null;
        try {
            user = service.deleteUserByName(userName);
        } catch (UserServiceException e) {
            fail(e.getMessage());
        }

        // Then
        assertEquals(userName, user.getName());
        assertEquals(userDeleted.getCoins(), user.getCoins());
        verify(repository, times(1)).findUserByName(userName);
        verify(repository, times(1)).deleteUserByName(userName);
    }

    @Test
    public void givenNonExistingUserName_whenDeleteUserByName_thenThrowsNotFoundUserServiceException() {
        when(repository.findUserByName("juliana")).thenReturn(Optional.empty());

        // Given
        String userName = "juliana";

        // When
        try {
            service.deleteUserByName(userName);
        } catch (UserServiceException e) {
            // Then
            assertEquals(UserServiceException.NOT_FOUND, e.getMessage());
        }

        verify(repository, times(1)).findUserByName(userName);
        verify(repository, times(0)).deleteUserByName(userName);
    }
}
