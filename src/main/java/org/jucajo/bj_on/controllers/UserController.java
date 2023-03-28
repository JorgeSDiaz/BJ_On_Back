package org.jucajo.bj_on.controllers;

import org.jucajo.bj_on.models.User;
import org.jucajo.bj_on.persistence.UserServiceException;
import org.jucajo.bj_on.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = "/usr/v1.0")
public class UserController {

    @Autowired
    UserService userService;


    @PostMapping(path = "/")
    public ResponseEntity<?> createUser(@RequestBody User newUser){
        try {
            return new ResponseEntity<User>(userService.createUser(newUser), HttpStatus.CREATED);
        } catch (UserServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }

    }

    @GetMapping(path = "/")
    public ResponseEntity<?> getAllUser(){
        try {
            return new ResponseEntity<>(userService.findAllUsers(), HttpStatus.OK);
        } catch (UserServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/{name}")
    public ResponseEntity<?> getUserByName(@PathVariable(value = "name") String name){
        try {
            return new ResponseEntity<>(userService.findUserByName(name), HttpStatus.OK);
        } catch (UserServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/{name}")
    public ResponseEntity<?> deleteUserByName(@PathVariable(value = "name") String name){
        try {
            return new ResponseEntity<>(userService.deleteUserByName(name), HttpStatus.ACCEPTED);
        } catch (UserServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(path = "/{name}")
    public ResponseEntity<?> updateUser(@RequestBody User newUser,@PathVariable(value = "name") String name){
        try {
            return new ResponseEntity<>(userService.updateUser(newUser, name), HttpStatus.OK);
        } catch (UserServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_MODIFIED) ;
        }
    }
}
