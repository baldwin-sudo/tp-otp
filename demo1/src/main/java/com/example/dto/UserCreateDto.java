package com.example.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDto {
    @NotNull
    @NotEmpty
    private String name;
    @NotNull(message="email is required .")
    @NotEmpty
    @Email(message = "please provide valid email .")
    private String email;
    @NotNull(message="number is required .")
    @NotEmpty
    private String phoneNumber;


}
