package org.jucajo.bj_on.services;


import org.jucajo.bj_on.persistence.UserServiceException;
import org.jucajo.bj_on.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameServices {

    @Autowired
    GameRepository gameRepository;

    public void registerBet(int mount,String userName,String gameName){
        if (gameRepository.findGameByName(gameName).isEmpty()) {
            throw new UserServiceException(UserServiceException.NOT_FOUND);
        }



    }




}
