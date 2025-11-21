package com.example.service;

import com.example.bean.Auth;
import com.example.bean.User;
import com.example.dto.UserDto;
import com.example.repository.AuthRepository;
import com.example.repository.UserRepository;
import com.example.utils.SmsApiClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    SmsApiClient smsApiClient;

    @Autowired
    private AuthRepository authRepository;
    @Autowired
    private UserRepository userRepository;
    private String generateOtp(){

        return "otp";
    }

    public int createOtp(String email) throws Exception {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
          throw new Exception(" no such user found with number " + email);
        }

        User user = userOpt.get();
        System.out.printf("user : "+ user);

        // delete all existing otps for this user before sending
        // because unverified otps remain in the db
        authRepository.deleteAllByIdUser(user.getId());

        // Use constructor - it sets createdOn
        String otp =generateOtp();
        // TODO: hash the otp
        String hashedOtp = hashOtp(otp);
        Auth auth = new Auth(otp, hashedOtp, user.getId());

        authRepository.save(auth);
        // send otp
        smsApiClient.sendSms(user.getPhoneNumber(), otp);
        if (auth.getId() != null) {
            System.out.println("OTP created: " + auth.getId());
            return 1;
        } else {
           throw new Exception("Error Creating OTP ");
        }
    }

    private String hashOtp(String otp) {
        return otp;
    }

    public int verifyOtp(String otp, String email) throws Exception    {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) throw new Exception("no such user exists: " + email);

        User user = userOpt.get();
        Optional<Auth> authOpt = authRepository.findByOtpClairAndIdUser(otp, user.getId());
        if  (authOpt.isEmpty()) throw new Exception("no otp found : " + email);

        Auth auth = authOpt.get();
        if (!auth.getOtpClair().equals(otp)) {
            throw new Exception("Invalid OTP");
        }
        authRepository.delete(auth);
        return  1;
    }
}