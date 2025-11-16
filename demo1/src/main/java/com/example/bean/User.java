package com.example.bean;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data  // Génère automatiquement equals(), hashCode(), toString(), getters, setters
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @Column(unique = true)
    private String email;

    @Column(name = "phone_number",unique = true)
    private String phoneNumber;


   }