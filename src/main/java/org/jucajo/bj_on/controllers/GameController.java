package org.jucajo.bj_on.controllers;

import org.jucajo.bj_on.models.Bet;
import org.jucajo.bj_on.models.Box;
import org.jucajo.bj_on.models.User;
import org.jucajo.bj_on.persistence.GameControllerException;
import org.jucajo.bj_on.services.GameService;
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






@RestController
@RequestMapping(path = "/game/v1.0")
public class GameController {
    
    private boolean canRegistryBet = false;

    private long startTime;

    private long elapsedTime;



    @Autowired
    SimpMessagingTemplate msgt;

    @Autowired
    GameService gameService; 

    @PostMapping("/player")
    public  ResponseEntity<?>  addNewPlayer(@RequestBody User newUser) throws GameControllerException{
        try{
            gameService.addNewPlayer(newUser);
            msgt.convertAndSend("/topic/players",newUser);
            return new ResponseEntity<>(GameControllerException.ON_GAME, HttpStatus.CREATED);

        }catch(GameControllerException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.FORBIDDEN);

        }
        
        
    }


    @GetMapping("/player")
    public ResponseEntity<?> getPlayers() throws GameControllerException{
        try{
            return new ResponseEntity<>(gameService.getPlayers(),HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
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
            try{
                elapsedTime = System.currentTimeMillis() - startTime;
                gameService.registerBet(newBox);
                msgt.convertAndSend("/topic/registerbet",gameService.getBets());
                return new ResponseEntity<>(GameControllerException.REGISTER_BET, HttpStatus.CREATED);

            }catch(GameControllerException e){
                return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);

            }  
        }
        else{
            return new ResponseEntity<>(GameControllerException.TIME_FINISHED, HttpStatus.NOT_FOUND);
        }
        

    }




    @GetMapping("/cronometer")
    public ResponseEntity<?> start(){
        if(!canRegistryBet){
            startTime = System.currentTimeMillis();
            canRegistryBet = true;
            return new ResponseEntity<>("TIME TO BET",HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("THE TIME OF BET IS RUNNING",HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/elapsedtime")
    public ResponseEntity<?> elapsedTime(){
        elapsedTime = System.currentTimeMillis() - startTime;
        return new ResponseEntity<>(elapsedTime,HttpStatus.OK);

    }





}
