package com.serenitydojo.wordle.microservices.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@Table(name = "players",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank
    @Column(nullable = false)
    private String password; // Store encrypted passwords for security

    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String role;

    @NotBlank
    @Column(nullable = false)
    private String country;

    @Column
    private boolean receiveUpdates;

    public Player(String username, String password, String email, String country, boolean receiveUpdates) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = "ROLE_PLAYER";
        this.country = country;
        this.receiveUpdates = receiveUpdates;
    }
}
