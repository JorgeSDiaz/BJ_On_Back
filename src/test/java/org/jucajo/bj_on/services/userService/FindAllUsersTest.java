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

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FindAllUsersTest {
    @Mock
    private UserRepository repository;
    @InjectMocks
    private UserService service;
    private List<User> usersFilledList, usersEmptyList;

    @Before
    public void setUp() {
        usersFilledList = List.of(
                new User("jorge", 0),
                new User("laura", 100),
                new User("camilo", 50),
                new User("julian", 1_000),
                new User("juliana", 1_000_000),
                new User("daniela", 2_000_000)
        );

        usersEmptyList = new ArrayList<>();
    }

    @Test
    public void givenUserRepositoryFilled_whenFindAllUsers_ThenReturnListOfUsers() {
        when(repository.findAll()).thenReturn(usersFilledList);

        // Given
        // UserFilledList config in SetUp

        // When
        List<User> returnedList = new ArrayList<>();
        try {
            returnedList = service.findAllUsers();
        } catch (UserServiceException e) {
            fail(e.getMessage());
        }

        // Then
        assertEquals(usersFilledList.size(), returnedList.size());

        for (int userIndex = 0; userIndex < usersFilledList.size(); userIndex++) {
            assertEquals(usersFilledList.get(userIndex).getName(), returnedList.get(userIndex).getName());
        }

        for (int userIndex = 0; userIndex < usersFilledList.size(); userIndex++) {
            assertEquals(usersFilledList.get(userIndex).getCoins(), returnedList.get(userIndex).getCoins());
        }

        for (int userIndex = 0; userIndex < usersFilledList.size(); userIndex++) {
            assertEquals(usersFilledList.get(userIndex).getCreationDate(), returnedList.get(userIndex).getCreationDate());
        }

        verify(repository, times(1)).findAll();
    }

    @Test
    public void givenUserRepositoryEmpty_whenFindAllUsers_thenThrowsNotFoundUserServiceException() {
        when(repository.findAll()).thenReturn(usersEmptyList);

        // Given
        // UsersEmptyList config in SetUp

        try {
            // When
            service.findAllUsers();
        } catch (UserServiceException e) {
            // Then
            assertEquals(UserServiceException.NOT_FOUND, e.getMessage());
        }

        verify(repository, times(1)).findAll();
    }
}
