package org.jucajo.bj_on.services;

import org.jucajo.bj_on.models.User;
import org.jucajo.bj_on.persistence.UserException;
import org.jucajo.bj_on.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
     * @throws UserException No users in database
     */
    public List<User> findAllUsers() throws UserException {
        List<User> users = repository.findAll();
        if (users.isEmpty()) {
            throw new UserException(UserException.NOT_FOUND);
        }

        return users;
    }

    /**
     * Find user by the given id
     * @param userId User id
     * @return The searched user
     * @throws UserException User not found with the given id
     */
    public User findUserById(String userId) throws UserException {
        if (repository.findById(userId).isEmpty()) {
            throw new UserException(UserException.NOT_FOUND);
        }

        return repository.findById(userId).get();
    }

    /**
     * Find user by given name
     * @param userName Name of user searched
     * @return The searched user
     * @throws UserException User not found with the given name
     */
    public User findUserByName(String userName) throws UserException {
        if (repository.findUserByName(userName).isEmpty()) {
            throw new UserException(UserException.NOT_FOUND);
        }

        return repository.findUserByName(userName).get();
    }

    /**
     * Add a new user in database
     * @param newUser User
     * @return User registered in database
     * @throws UserException User already exists
     */
    public User createUser(User newUser) throws UserException {
        if (repository.findUserByName(newUser.getName()).isPresent()) {
            throw new UserException(UserException.ALREADY_EXISTS);
        }

        return repository.save(newUser);
    }

    /**
     * Update a user in the database
     * @param newUser User with updated information
     * @return User updated in database
     * @throws UserException User to update not found
     */
    public User updateUser(User newUser) throws UserException {
        if (repository.findUserByName(newUser.getName()).isEmpty()) {
            throw new UserException(UserException.NOT_FOUND);
        }

        return repository.save(newUser);
    }

    /**
     * Delete user in database
     * @param userName Name of the user to be deleted
     * @return Deleted user information in database
     * @throws UserException User to be deleted is not found
     */
    public User deleteUserByName(String userName) throws UserException {
        if (repository.findUserByName(userName).isEmpty()) {
            throw new UserException(UserException.NOT_FOUND);
        }

        return repository.deleteUserByName(userName);
    }
}
