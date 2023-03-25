package org.jucajo.bj_on.repositories;

import org.jucajo.bj_on.models.User;
import org.jucajo.bj_on.persistence.UserException;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findUserByName(String name) throws UserException;
    User deleteUserByName(String name) throws UserException;
}
