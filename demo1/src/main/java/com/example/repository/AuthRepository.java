package com.example.repository;

import com.example.bean.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<Auth, Long> {
    @Query("SELECT a FROM Auth a WHERE a.otpClair = :otp AND a.idUser = :idUser")
    public Optional<Auth> findByOtpClairAndIdUser(String otp,Long idUser    );
}
