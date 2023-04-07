package org.jucajo.bj_on.services;

import java.util.concurrent.ConcurrentHashMap;

import org.jucajo.bj_on.models.Bet;
import org.jucajo.bj_on.models.Box;
import org.jucajo.bj_on.models.User;
import org.jucajo.bj_on.persistence.GameControllerException;
import org.springframework.stereotype.Service;


@Service
public class GameService {
    ConcurrentHashMap<String,User> ListPlayer = new ConcurrentHashMap<>();
    ConcurrentHashMap<String,Box> boxBets = new ConcurrentHashMap<>();




    public void addNewPlayer(User newUser) throws GameControllerException{
        if(ListPlayer.size() == 4){
            throw new GameControllerException(GameControllerException.GAME_FULL);
        }
        ListPlayer.put(newUser.getName(), newUser);
    }


    public ConcurrentHashMap<String,User> getPlayers(){
        return ListPlayer;
    }


    public void registerBet(Box newBox)throws GameControllerException{
        if(boxBets.containsKey(newBox.getCard())){
            //EN EL CASO DE QUE  QUIERA CAMBIAR SU APUESTA YA HECHA
            if(boxBets.get(newBox.getCard()).getBet().getOwner().equals(newBox.getBet().getOwner())){
                boxBets.put(newBox.getCard(), newBox);
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
                boxBets.put(newBox.getCard(), newBox);
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

    public ConcurrentHashMap<String,Box> getBets(){
        return boxBets;
    }





    
}