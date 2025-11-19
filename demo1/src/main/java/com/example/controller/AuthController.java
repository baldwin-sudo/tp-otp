package com.example.controller;


import com.example.dto.VerifyOtpDto;

import com.example.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*",methods = {RequestMethod.PUT,RequestMethod.POST,RequestMethod.GET,RequestMethod.OPTIONS}) // allow only this origin

public class AuthController {
   private AuthService authService;
   @Autowired
   public AuthController(AuthService authService) {
       this.authService = authService;
   }
    @PostMapping("/send-otp")
    public ResponseEntity<?> requestingOtp(@RequestParam String phoneNumber){
        try {
            if(authService.createOtp(phoneNumber)==1){
                return ResponseEntity.ok().body("otp sent . check your phone");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().body("error sending otp .");




    }
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyOtpDto verifyOtpDto){
        try {
            if (authService.verifyOtp(verifyOtpDto.getOtp(),verifyOtpDto.getPhoneNumber())==1){
                return ResponseEntity.ok().body("otp verified");

            }
              throw new Exception("error verifying otp");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body("error verifying otp");
        }
    }


}
