package com.PersonalProject.QuickBid.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false , unique = true)
    private String email;

    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false , unique = true)
    private String phoneNo;
    
    @Column(nullable = false)
    private LocalDate dob;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;
    
    public enum Role {
        USER, ADMIN
    }
    
}