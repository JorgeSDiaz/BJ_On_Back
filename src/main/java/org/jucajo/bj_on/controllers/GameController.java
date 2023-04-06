package org.jucajo.bj_on.controllers;

import org.jucajo.bj_on.models.Bet;
import org.jucajo.bj_on.models.Box;
import org.jucajo.bj_on.models.User;
import org.jucajo.bj_on.persistence.GameControllerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

import java.util.concurrent.ConcurrentHashMap;




@RestController
@RequestMapping(path = "/game/v1.0")
public class GameController {
    ConcurrentHashMap<String,User> ListPlayer = new ConcurrentHashMap<>();
    ConcurrentHashMap<String,Box> boxBets = new ConcurrentHashMap<>();

    private boolean canRegistryBet = false;



    @Autowired
    SimpMessagingTemplate msgt;

    @PostMapping("/player")
    public  ResponseEntity<?>  addNewPlayer(@RequestBody User newUser) throws GameControllerException{
        if(ListPlayer.size() == 4){
            return new ResponseEntity<>(GameControllerException.GAME_FULL, HttpStatus.CONFLICT);


        }
        else{
            ListPlayer.put(newUser.getName(),newUser);
            msgt.convertAndSend("/topic/players", newUser);
            return new ResponseEntity<>(ListPlayer, HttpStatus.OK);
        }
        
    }


    @Scheduled(fixedRate = 15000)
    public void scheduledTask() {
        if(canRegistryBet){
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        canRegistryBet = false;
    }


    @PostMapping("/betbox")
    public ResponseEntity <?> registerBet(@RequestBody Box newBox) throws GameControllerException{
        if(canRegistryBet){
            if(boxBets.containsKey(newBox.getCard())){
                //EN EL CASO DE QUE  QUIERA CAMBIAR SU APUESTA YA HECHA
                if(boxBets.get(newBox.getCard()).getBet().getOwner().equals(newBox.getBet().getOwner())){
                    boxBets.put(newBox.getCard(), newBox);
                    msgt.convertAndSend("/topic/betextra", boxBets);
                    return new ResponseEntity<>("BET UPDATE", HttpStatus.ACCEPTED);
                }
                else{
                    //SI VA A APOSTAR OTRA VEZ o MODIFICAR OTRA APUESTA YA HECHA
                    return new ResponseEntity<>(GameControllerException.NOT_UPDATE_OTHER_BETS_OR_UPDATE_AGAIN, HttpStatus.NOT_ACCEPTABLE);
                }
            }
            else{
                boxBets.put(newBox.getCard(), newBox);
                msgt.convertAndSend("/topic/betextra", boxBets);
                return new ResponseEntity<>("BET CREATED", HttpStatus.ACCEPTED);

            }
        }
        else{
            return new ResponseEntity<>(GameControllerException.TIME_FINISHED, HttpStatus.NOT_FOUND);
        }
        

    }




    @GetMapping("/cronometer")
    public ResponseEntity<?> start(){
        if(!canRegistryBet){
            canRegistryBet = true;
            return new ResponseEntity<>("TIME TO BET",HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("THE TIME OF BET IS RUNNING",HttpStatus.BAD_REQUEST);
        }
    }


    









    


    
}
