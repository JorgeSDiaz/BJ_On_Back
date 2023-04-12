package org.jucajo.bj_on.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jucajo.bj_on.models.Box;
import org.jucajo.bj_on.models.Token;
import org.jucajo.bj_on.models.User;
import org.jucajo.bj_on.persistence.GameControllerException;
import org.jucajo.bj_on.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import static org.jucajo.bj_on.shared.ResponseJson.*;


@RestController
@RequestMapping(path = "/v1/game")
@Tag(name = "game-controller", description = "Game APIrest")
public class GameController {

    private boolean canRegistryBet = true;

    private long startTime;

    private long elapsedTime;


    @Autowired
    SimpMessagingTemplate msgt;
    @Autowired
    GameService gameService;
    @Autowired
    private ObjectMapper objectMapper;

    @Operation(
            description = "Add user to the room and send a topic /topic/players the new user ",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "CONNECTION SUCCESFULL",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = User.class)

                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "GAME FULL",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "error":\s
                                                                {
                                                                    "code" : 409,\s
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
    public ResponseEntity<?> addNewPlayer(@RequestBody User newUser) throws GameControllerException {
        try {
            gameService.addNewPlayer(newUser);
            msgt.convertAndSend("/topic/players", newUser);
            return new ResponseEntity<>(GameControllerException.ON_GAME, HttpStatus.OK);

        } catch (GameControllerException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);

        }


    }


    @Operation(
            description = "Get all users that at the room",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            type = "array",
                                            implementation = User.class
                                    )
                            )
                    )
            }


    )

    @GetMapping("/player")
    public ResponseEntity<?> getPlayers() throws GameControllerException {
        return new ResponseEntity<>(gameService.getPlayers(), HttpStatus.OK);
    }


    @Scheduled(fixedRate = 15000)
    public void scheduledTask() {
        if (canRegistryBet) {
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        canRegistryBet = false;
    }

    @Operation(
            description = "Register bet extra and send a topic /topic/registerbet the new bet ",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "BET REGISTERED",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Box.class)

                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "YOU CANT UPDATE BETS OF THE OTHER PLAYER",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "error":\s
                                                                {
                                                                    "code" : 409,\s
                                                                    "message":"YOU CANT UPDATE BETS OF THE OTHER PLAYER"
                                                                }
                                                            }
                                                            """
                                            ),
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "YOU ONLY BET FOR A ONE NUMBER",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "error":\s
                                                                {
                                                                    "code" : 409,\s
                                                                    "message":"YOU ONLY BET FOR A ONE NUMBER"
                                                                }
                                                            }
                                                            """
                                            ),
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "THE TIME OF BET FINISHED",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "error":\s
                                                                {
                                                                    "code" : 409,\s
                                                                    "message":"THE TIME OF BET FINISHED"
                                                                }
                                                            }
                                                            """
                                            ),
                                    }
                            )
                    )
            }

    )


    @PostMapping("/betBox/{id}/owner/{owner}")
    public ResponseEntity<?> registerBet(@RequestBody Token newToken, @PathVariable(value = "id") String id,
                                         @PathVariable(value = "owner") String owner) throws GameControllerException {
        try {
            System.out.println(objectMapper.writeValueAsString(newToken));
            gameService.registerBet(id, owner, newToken);
            msgt.convertAndSend("/topic/registerbet", "{\"id\":" + id + ", \"token\": " + objectMapper.writeValueAsString(gameService.getToken(id)) + "}");
            return new ResponseEntity<>(GameControllerException.REGISTER_BET, HttpStatus.CREATED);
        } catch (GameControllerException e) {
            return new ResponseEntity<>(error(409, e.getMessage()), HttpStatus.CONFLICT);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>(error(500, "Error no Controlado"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Operation(
            description = "Get all bets",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            type = "array",
                                            implementation = Box.class
                                    )
                            )
                    )
            }
    )


    @GetMapping("/betbox")
    public ResponseEntity<?> getBets() {
        return new ResponseEntity<>(gameService.getBets(), HttpStatus.OK);
    }


    @Operation(
            description = "Start 15 seconds of bets and send topic /topic/elapsedtime the message TIME TO BET",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "elapsedTime":\s
                                                                {
                                                                    "code" : 200,\s
                                                                    "message":"TIME TO BET"
                                                                }
                                                            }
                                                            """
                                            ),
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "elapsedTime":\s
                                                                {
                                                                    "code" : 400,\s
                                                                    "message":"THE TIME OF BET IS RUNNING"
                                                                }
                                                            }
                                                            """
                                            ),
                                    }
                            )
                    )
            }
    )

    @GetMapping("/cronometer")
    public ResponseEntity<?> start() {
        if (!canRegistryBet) {
            startTime = System.currentTimeMillis();
            canRegistryBet = true;
            msgt.convertAndSend("/topic/elapsedtime", "TIME TO BET");
            return new ResponseEntity<>("TIME TO BET", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("THE TIME OF BET IS RUNNING", HttpStatus.BAD_REQUEST);
        }
    }


    @Operation(
            description = "get elapsed time and send a topic /topic/elapsedtime the following: if the elapsed time is greater than 15000 then send TIME TO BET FINISHED, otherwise send the elapsed time  ",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "elapsedTime":\s
                                                                {
                                                                    "code" : 200,\s
                                                                    "message":"15000"
                                                                }
                                                            }
                                                            """
                                            ),
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "elapsedTime":\s
                                                                {
                                                                    "code" : 200,\s
                                                                    "message":"5676"
                                                                }
                                                            }
                                                            """
                                            ),
                                    }
                            )
                    )
            }
    )


    @GetMapping("/elapsedtime")
    public ResponseEntity<?> elapsedTime() {
        elapsedTime = System.currentTimeMillis() - startTime;
        if (elapsedTime > 15000) {
            canRegistryBet = false;
            elapsedTime = 0;
            msgt.convertAndSend("/topic/elapsedtime", "TIME TO BET FINISHED");
            return new ResponseEntity<>(15000, HttpStatus.OK);
        } else {
            msgt.convertAndSend("/topic/elapsedtime", elapsedTime);
            return new ResponseEntity<>(elapsedTime, HttpStatus.OK);

        }
    }


}
