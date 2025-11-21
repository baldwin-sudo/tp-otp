package com.example.controller;

import com.example.utils.SmsApiClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*",methods = {RequestMethod.PUT,RequestMethod.POST,RequestMethod.GET,RequestMethod.OPTIONS}) // allow only this origin

public class DefaultController {
    SmsApiClient smsApiClient;
    @Autowired
    public DefaultController(SmsApiClient smsApiClient) {
        this.smsApiClient = smsApiClient;
    }
    @GetMapping("/health-api")
    public ResponseEntity<?> getApi(){
        return  ResponseEntity.status(HttpStatus.OK).body("healthy");
    }
    @GetMapping("/health-message-server")
    public ResponseEntity<?> getMessageServer(){
        // TODO: send healthy req to the server
        try {

            System.out.println( smsApiClient.ping());
            return ResponseEntity.ok(smsApiClient.ping());
        //    JSONObject jsonObject = get("http://localhost:8080/health");

        } catch (Exception e) {
        return  ResponseEntity.status(HttpStatus.OK).body("not healthy");
        }


    }
}
