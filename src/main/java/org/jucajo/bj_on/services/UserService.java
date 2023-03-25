package org.jucajo.bj_on.services;

import org.jucajo.bj_on.models.User;
import org.jucajo.bj_on.persistence.UserException;
import org.jucajo.bj_on.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository repository;

    public List<User> findAllUsers() throws UserException {
        List<User> users = repository.findAll();
        if (users.isEmpty()) {
            throw new UserException(UserException.NOT_FOUND);
        }

        return users;
    }

    public User findUserById(String userId) throws UserException {
        if (repository.findById(userId).isEmpty()) {
            throw new UserException(UserException.NOT_FOUND);
        }

        return repository.findById(userId).get();
    }

    public User findUserByName(String userName) throws UserException {
        if (repository.findUserByName(userName).isEmpty()) {
            throw new UserException(UserException.NOT_FOUND);
        }

        return repository.findUserByName(userName).get();
    }

    public User createUser(User newUser) throws UserException {
        if (repository.findUserByName(newUser.getName()).isPresent()) {
            throw new UserException(UserException.ALREADY_EXISTS);
        }

        return repository.save(newUser);
    }

    public User updateUser(User newUser) throws UserException {
        if (repository.findUserByName(newUser.getName()).isEmpty()) {
            throw new UserException(UserException.NOT_FOUND);
        }

        return repository.save(newUser);
    }

    public User deleteUserByName(String userName) throws UserException {
        if (repository.findUserByName(userName).isEmpty()) {
            throw new UserException(UserException.NOT_FOUND);
        }

        return repository.deleteUserByName(userName);
    }
}
