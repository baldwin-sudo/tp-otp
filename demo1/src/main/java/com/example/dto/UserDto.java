package com.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.NumberFormat;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    @NotNull
    @NotEmpty
    private String name;
    @NotNull
    @NotEmpty
    @Email
    private String email;
    @NotNull
    @NotEmpty
    private String phoneNumber;

}
