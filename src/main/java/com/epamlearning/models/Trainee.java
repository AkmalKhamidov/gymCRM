package com.epamlearning.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Data
public class Trainee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    @Column
    private String address;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "trainee")
//    @JoinColumn(name = "trainee_id")
    private List<Training> training;

    @Override
    public String toString() {
        return "Trainee ID: " + id +
                "\nName: " + user.getFirstName() + " " + user.getLastName() +
                "\nDate of Birth: " + dateOfBirth +
                "\nAddress: " + address +
                "\nUser ID: " + user.getId();
    }
}
