package org.jucajo.bj_on.services;

import org.jucajo.bj_on.models.Box;
import org.jucajo.bj_on.models.Token;
import org.jucajo.bj_on.models.User;
import org.jucajo.bj_on.persistence.GameControllerException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


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


    public void registerBet(String id, String owner, Token newToken)throws GameControllerException{
        if(boxBets.containsKey(id)){
            //EN EL CASO DE QUE  QUIERA CAMBIAR SU APUESTA YA HECHA
            if(boxBets.get(id).getOwner().equals(owner)){
                boxBets.put(id, new Box(id, owner, newToken));
            }
            else{
                //MODIFICAR OTRA APUESTA YA HECHA
                throw new GameControllerException(GameControllerException.NOT_UPDATE_OTHER_BETS);
            }
        }
        else{
            //si va a apostar otra vez
            if(ownerYetBet(owner)){
                throw new GameControllerException(GameControllerException.ONLY_BET);

            }
            else{
                boxBets.put(id, new Box(id, owner, newToken));
            }
            
        }

    }


    private boolean ownerYetBet(String owner){
        for(Box box: boxBets.values()){
            if(box.getOwner().equals(owner)){
                return true;
            }
        }
        return false;
    }

    public List<Box> getBets(){
        List<Box> valuelist = new ArrayList<>(boxBets.values());
        return valuelist;
    }


    public Token getToken(String id) {
        return boxBets.get(id).getToken();
    }
}
