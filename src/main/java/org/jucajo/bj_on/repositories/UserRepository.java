package org.jucajo.bj_on.repositories;

import org.jucajo.bj_on.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findUserByName(String name);
    User deleteUserByName(String name);
}
