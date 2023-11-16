package com.epamlearning.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Trainer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "specialization", referencedColumnName = "id")
    private TrainingType specialization;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany
    @JoinColumn(name = "trainer_id")
    private List<Training> training;

    @Override
    public String toString() {
        return "Trainer ID: " + id +
                "\nName: " + user.getFirstName() + " " + user.getLastName() +
                "\nSpecialization: " + specialization.getTrainingTypeName() +
                "\nUser ID: " + user.getId();
    }

}
