package org.jucajo.bj_on.services;

import org.jucajo.bj_on.models.User;
import org.jucajo.bj_on.persistence.UserServiceException;
import org.jucajo.bj_on.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * User services
 * @author Jorge David Saenz Diaz
 * @version 1.0
 */
@Service
public class UserService {
    @Autowired
    UserRepository repository;

    /**
     * Find all saved users in database
     * @return list of registered users in database
     * @throws UserServiceException No users in database
     */
    public List<User> findAllUsers() throws UserServiceException {
        List<User> users = repository.findAll();
        if (users.isEmpty()) {
            throw new UserServiceException(UserServiceException.NOT_FOUND);
        }

        return users;
    }

    /**
     * Find user by given name
     * @param userName Name of user searched
     * @return The searched user
     * @throws UserServiceException User not found with the given name
     */
    public User findUserByName(String userName) throws UserServiceException {
        Optional<User> user = repository.findUserByName(userName);
        if (user.isEmpty()) {
            throw new UserServiceException(UserServiceException.NOT_FOUND);
        }

        return user.get();
    }

    /**
     * Add a new user in database
     * @param newUser User
     * @return User registered in database
     * @throws UserServiceException User already exists
     */
    public User createUser(User newUser) throws UserServiceException {
        if (repository.findUserByName(newUser.getName()).isPresent()) {
            throw new UserServiceException(UserServiceException.ALREADY_EXISTS);
        }

        return repository.save(newUser);
    }

    /**
     * Update a user in the database
     * @param newUser User with updated information
     * @return User updated in database
     * @throws UserServiceException User to update not found
     */
    public User updateUser(User newUser, String username) throws UserServiceException {
        if (repository.findUserByName(username).isEmpty()) {
            throw new UserServiceException(UserServiceException.NOT_FOUND);
        }

        User userToUpdate = repository.findUserByName(username).get();
        userToUpdate.setName(newUser.getName());
        userToUpdate.setCoins(newUser.getCoins());

        return repository.save(userToUpdate);
    }
}
