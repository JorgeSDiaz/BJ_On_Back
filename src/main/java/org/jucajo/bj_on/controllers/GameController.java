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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;







@RestController
@RequestMapping(path = "/game/v1.0")
@Tag(name = "Game", description = "Game APIrest")
public class GameController {
    
    private boolean canRegistryBet = false;

    private long startTime;

    private long elapsedTime;



    @Autowired
    SimpMessagingTemplate msgt;

    @Autowired
    GameService gameService;
    
    @Operation(
        description = "Add user to the room",
        responses = {
             @ApiResponse(
                responseCode = "201",
                description = "CONNECTION SUCCESFULL",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = User.class)

                )
             ),
             @ApiResponse(
                responseCode = "403",
                description = "GAME FULL",
                content = @Content(
                    mediaType = "application/json",
                    examples = {
                        @ExampleObject(
                            value = """
                                    {
                                        "error":\s
                                        {
                                            "code" : 403,\s
                                            "message":"GAME FULL"
                                        }
                                    }
                                    """
                        ),
                    }
                )
             )
        }

    )

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





    @Operation(
        description = "Get all users that at the room",
        responses = {
             @ApiResponse(
                responseCode = "201",
                description = "CONNECTION SUCCESFULL",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                        type = "Object",
                        implementation = User.class
                    )
                )
             )
        }
             

    )
    @GetMapping("/player")
    public ResponseEntity<?> getPlayers() throws GameControllerException{
        return new ResponseEntity<>(gameService.getPlayers(),HttpStatus.OK);
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


    @GetMapping("/betbox")
    public ResponseEntity<?> getBets(){
        return new ResponseEntity<>(gameService.getBets(), HttpStatus.OK);
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
        if(elapsedTime > 15000){
            canRegistryBet = false;
            elapsedTime = 0;
            msgt.convertAndSend("/topic/elapsedtime","TIME TO BET FINISHED");
            return new ResponseEntity<>(15000,HttpStatus.OK);
        }
        return new ResponseEntity<>(elapsedTime,HttpStatus.OK);

    }





}
