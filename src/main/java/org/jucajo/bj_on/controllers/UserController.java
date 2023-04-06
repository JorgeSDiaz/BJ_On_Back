package org.jucajo.bj_on.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.jucajo.bj_on.models.User;
import org.jucajo.bj_on.persistence.UserServiceException;
import org.jucajo.bj_on.request.UserRequest;
import org.jucajo.bj_on.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.jucajo.bj_on.shared.ResponseJson.error;


@RestController
@RequestMapping(path = "/v1/users")
@Tag(name = "Users", description = "Users APIRest")
public class UserController {

    @Autowired
    UserService userService;

    @Operation(
            description = "Create user",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "User was created",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = User.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "User already exists",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "error":\s
                                                                    {
                                                                        "code": 400,\s
                                                                        "message": "USER ALREADY EXISTS"
                                                                    }
                                                            }
                                                            """
                                            ),
                                    }
                            )
                    )
            }
    )
    @PostMapping(path = "/")
    public ResponseEntity<?> createUser(@RequestBody UserRequest newUser){
        try {
            return new ResponseEntity<User>(userService.createUser(new User(newUser.getName(), newUser.getCoins())),
                    HttpStatus.CREATED);
        } catch (UserServiceException e) {
            return new ResponseEntity<>(error(400, e.getMessage()), HttpStatus.BAD_REQUEST);
        }

    }

    @Operation(
            description = "Get all registered users",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = User.class, type = "array")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "error":\s
                                                                    {
                                                                        "code": 404,\s
                                                                        "message": "USER NOT FOUND"
                                                                    }
                                                            }
                                                            """
                                            ),
                                    }
                            )
                    )
            }
    )
    @GetMapping(path = "/")
    public ResponseEntity<?> getAllUser(){
        try {
            return new ResponseEntity<>(userService.findAllUsers(), HttpStatus.OK);
        } catch (UserServiceException e) {
            return new ResponseEntity<>(error(404, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            description = "Get user given given the name",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = User.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "error":\s
                                                                    {
                                                                        "code": 404,\s
                                                                        "message": "USER NOT FOUND"
                                                                    }
                                                            }
                                                            """
                                            ),
                                    }
                            )
                    )
            }
    )
    @GetMapping(path = "/{name}")
    public ResponseEntity<?> getUserByName(@PathVariable(value = "name") String name){
        try {
            return new ResponseEntity<>(userService.findUserByName(name), HttpStatus.OK);
        } catch (UserServiceException e) {
            return new ResponseEntity<>(error(404, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            description = "Update user data",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = User.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "304",
                            description = "User not modified",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "error":\s
                                                                    {
                                                                        "code": 304,\s
                                                                        "message": "USER NOT MODIFIED"
                                                                    }
                                                            }
                                                            """
                                            ),
                                    }
                            )
                    )
            }
    )
    @PutMapping(path = "/{name}")
    public ResponseEntity<?> updateUser(@RequestBody UserRequest newUser,@PathVariable(value = "name") String name){
        try {
            return new ResponseEntity<>(userService.updateUser(new User(newUser.getName(), newUser.getCoins()), name),
                    HttpStatus.OK);
        } catch (UserServiceException e) {
            return new ResponseEntity<>(error(304, e.getMessage()), HttpStatus.NOT_MODIFIED) ;
        }
    }
}
