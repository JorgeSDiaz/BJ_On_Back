package org.jucajo.bj_on.repositories;

import org.jucajo.bj_on.models.Game;
import org.jucajo.bj_on.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface GameRepository  extends MongoRepository<Game,String> {

    Optional<User> findGameByName(String name);


}
