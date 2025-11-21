package com.example.controller;


import com.example.dto.VerifyOtpDto;

import com.example.service.AuthService;
import com.example.utils.SmsApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*",methods = {RequestMethod.PUT,RequestMethod.POST,RequestMethod.GET,RequestMethod.OPTIONS}) // allow only this origin

public class AuthController {
   private AuthService authService;
   private SmsApiClient smsApiClient        ;
   @Autowired
   public AuthController(AuthService authService,SmsApiClient smsApiClient) {
       this.authService = authService;
       this.smsApiClient = smsApiClient;
   }
    @PostMapping("/send-otp")
    public ResponseEntity<?> requestingOtp(@RequestParam String email){
        try {
            if(authService.createOtp(email)==1){
                return ResponseEntity.ok().body("otp sent . check your phone");
            }
            throw new Exception("error sending otp .");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(e.getMessage());
        }

    }
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyOtpDto verifyOtpDto){
        try {
            if (authService.verifyOtp(verifyOtpDto.getOtp(),verifyOtpDto.getEmail())==1){
                return ResponseEntity.ok().body("otp verified");

            }
              throw new Exception("error verifying otp");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }


}
