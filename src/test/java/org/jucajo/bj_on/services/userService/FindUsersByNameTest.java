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

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FindUsersByNameTest {
    @Mock
    private UserRepository repository;
    @InjectMocks
    private UserService service;
    private List<User> usersList;

    @Before
    public void setUp() {
        usersList = List.of(
                new User("jorge", 0),
                new User("laura", 100)
        );
    }

    @Test
    public void givenExistingUserName_whenFindUserByName_thenReturnUser() {
        when(repository.findUserByName("jorge")).thenReturn(Optional.ofNullable(usersList.get(0)));

        // Given
        String userName = "jorge";

        // When
        User userReturned = null;
        try {
            userReturned = service.findUserByName(userName);
        } catch (UserServiceException e) {
            fail(e.getMessage());
        }

        // Then
        assertEquals(usersList.get(0).getName(), userReturned.getName());
        assertEquals(usersList.get(0).getCoins(), userReturned.getCoins());
        verify(repository, times(1)).findUserByName(userName);
    }

    @Test
    public void givenNonExistingUserName_whenFindByUserName_thenThrowsNotFoundUserServiceException() {
        when(repository.findUserByName("camilo")).thenReturn(Optional.empty());

        //Given
        String userName = "camilo";

        try {
            // When
            service.findUserByName(userName);
        } catch (UserServiceException e) {
            // Then
            assertEquals(UserServiceException.NOT_FOUND, e.getMessage());
        }

        verify(repository, times(1)).findUserByName(userName);
    }
}
