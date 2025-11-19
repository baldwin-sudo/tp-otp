package com.example.service;

import com.example.bean.Auth;
import com.example.bean.User;
import com.example.dto.UserDto;
import com.example.repository.AuthRepository;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private UserRepository userRepository;
    private String createOtpFromServer(String phoneNumber){
        return "otp";
    }
    public int createOtp(String phoneNumber) throws Exception {
        Optional<User> userOpt = userRepository.findByPhoneNumber(phoneNumber);

        if (userOpt.isEmpty()) {
            System.out.println("no such user exists: " + phoneNumber);
            return 0;
        }

        User user = userOpt.get();

        // Use constructor - it sets createdOn
        String otp =createOtpFromServer(user.getPhoneNumber());
        // TODO: hash the otp
        String hashedOtp = otp;
        Auth auth = new Auth(otp, hashedOtp, user.getId());

        authRepository.save(auth);

        if (auth.getId() != null) {
            System.out.println("OTP created: " + auth.getId());
            return 1;
        } else {
            System.out.println("Error creating OTP");
            return 0;
        }
    }

    public int verifyOtp(String otp, String phoneNumber) throws Exception    {
        Optional<User> userOpt = userRepository.findByPhoneNumber(phoneNumber);
        if (userOpt.isEmpty()) return 0;

        User user = userOpt.get();
        Optional<Auth> authOpt = authRepository.findByOtpClairAndIdUser(otp, user.getId());
        if  (authOpt.isEmpty()) return 0;
        Auth auth = authOpt.get();
        authRepository.delete(auth);
        return  1;
    }
}