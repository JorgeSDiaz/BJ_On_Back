package org.jucajo.bj_on.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.jucajo.bj_on.models.Box;
import org.jucajo.bj_on.models.User;
import org.jucajo.bj_on.persistence.GameControllerException;
import org.springframework.stereotype.Service;


@Service
public class GameService {
    ConcurrentHashMap<String,User> ListPlayer = new ConcurrentHashMap<>();
    ConcurrentHashMap<String,Box> boxBets = new ConcurrentHashMap<>();




    public void addNewPlayer(User newUser) throws GameControllerException{
        if(ListPlayer.size() == 3){
            throw new GameControllerException(GameControllerException.GAME_FULL);
        }
        ListPlayer.put(newUser.getName(), newUser);
    }


    public List<User> getPlayers(){
        List<User> valuelist = new ArrayList<>(ListPlayer.values());
        return valuelist;
    }


    public void registerBet(Box newBox)throws GameControllerException{
        if(boxBets.containsKey(newBox.getId())){
            //EN EL CASO DE QUE  QUIERA CAMBIAR SU APUESTA YA HECHA
            if(boxBets.get(newBox.getId()).getBet().getOwner().equals(newBox.getBet().getOwner())){
                boxBets.put(newBox.getId(), newBox);
            }
            else{
                //MODIFICAR OTRA APUESTA YA HECHA
                throw new GameControllerException(GameControllerException.NOT_UPDATE_OTHER_BETS);
            }
        }
        else{
            //si va a apostar otra vez
            if(ownerYetBet(newBox.getBet().getOwner())){
                throw new GameControllerException(GameControllerException.ONLY_BET);

            }
            else{
                boxBets.put(newBox.getId(), newBox);
            }
            
        }

    }


    private boolean ownerYetBet(String owner){
        for(Box box: boxBets.values()){
            if(box.getBet().getOwner().equals(owner)){
                return true;
            }
        }
        return false;
    }

    public List<Box> getBets(){
        List<Box> valuelist = new ArrayList<>(boxBets.values());
        return valuelist;
    }





    
}
