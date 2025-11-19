package com.example.bean;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "auth")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Auth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "otp_clair", length = 100)
    private String otpClair;

    @Column(name = "otp_hashe", length = 100)
    private String otpHashe;

    @Column(name = "id_user", nullable = false)
    private Long idUser;

    @Column(name = "created_on", nullable = false, updatable = false)
    private LocalDateTime createdOn = LocalDateTime.now();  // ← Set default here

    /**
     * JPA callback
     */
    @PrePersist
    protected void onCreate() {
        if (this.createdOn == null) {
            this.createdOn = LocalDateTime.now();
        }
    }

    /**
     * Constructor
     */
    public Auth(String otpClair, String otpHashe, Long idUser) {
        this.otpClair = otpClair;
        this.otpHashe = otpHashe;
        this.idUser = idUser;
        this.createdOn = LocalDateTime.now();  // ← Always set
    }
}