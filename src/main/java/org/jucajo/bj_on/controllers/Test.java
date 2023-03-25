package org.jucajo.bj_on.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

// reference -> https://www.youtube.com/watch?v=A_RWUcTqHBI&ab_channel=lemoncode21
@RestController
@RequestMapping(path = "/test")
@Tag(name = "test", description = "testing openapi")
public class Test {

    @Operation(summary = "Testing",
            description = "Testing Swagger")
    @RequestMapping(path = "/hello", method = RequestMethod.GET, produces = "application/json")
    public String helloWorld() {
        return "{\"data\": \"Hello World\"}";
    }
}
