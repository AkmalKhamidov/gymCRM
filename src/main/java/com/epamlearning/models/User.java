package com.epamlearning.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @JsonProperty("firstName")
    @Column(nullable = false)
    private String firstName;

    @JsonProperty("lastName")
    @Column(nullable = false)
    private String lastName;

    @JsonProperty("username")
    @Column(nullable = false)
    private String username;

    @JsonProperty("password")
    @Column(nullable = false)
    private String password;

    @JsonProperty("isActive")
    @Column(nullable = false)
    private boolean isActive;

    @Override
    public String toString() {
        return "User ID: " + id +
                "\nName: " + firstName + " " + lastName +
                "\nUsername: " + username +
                "\nPassword: " + password +
                "\nActive: " + isActive;
    }
}
