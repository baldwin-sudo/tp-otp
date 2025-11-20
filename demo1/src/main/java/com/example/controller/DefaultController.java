package com.example.controller;

import com.example.utils.HttpClientUtility;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.utils.HttpClientUtility.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*",methods = {RequestMethod.PUT,RequestMethod.POST,RequestMethod.GET,RequestMethod.OPTIONS}) // allow only this origin

public class DefaultController {

    public DefaultController() {}
    @GetMapping("/health-api")
    public ResponseEntity<?> getApi(){
        return  ResponseEntity.status(HttpStatus.OK).body("healthy");
    }
    @GetMapping("/health-message-server")
    public ResponseEntity<?> getMessageServer(){
        // TODO: send healthy req to the server
        try {
            JSONObject response = HttpClientUtility.get("http://dosipa.univ-brest.fr/ping");
            String message = response.getString("status");
            if (message!=null  && message.equals("OK")) {

            return  ResponseEntity.status(HttpStatus.OK).body("healthy");

            }
            throw  new Exception("unhealthy");
        //    JSONObject jsonObject = get("http://localhost:8080/health");

        } catch (Exception e) {
        return  ResponseEntity.status(HttpStatus.OK).body("not healthy");
        }

    }
}
