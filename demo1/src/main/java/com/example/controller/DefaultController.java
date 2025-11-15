package com.example.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*",methods = {RequestMethod.PUT,RequestMethod.POST,RequestMethod.GET,RequestMethod.OPTIONS}) // allow only this origin

public class DefaultController {
    public DefaultController() {}
    @GetMapping("/health")
    public ResponseEntity<?> getMapping(){
        return  ResponseEntity.status(HttpStatus.OK).body("healthy");
    }
}
