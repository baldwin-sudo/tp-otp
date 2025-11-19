package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Data structure to represent the request body of the /api/auth/verify-otp routes .

 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifyOtpDto {
    private String phoneNumber;
    private String otp;
}