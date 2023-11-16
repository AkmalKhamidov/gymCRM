package com.epamlearning.models;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "trainee_id")
    private Trainee trainee;

    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    @Column(nullable = false)
    private String trainingName;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date trainingDate;

    @Column(nullable = false)
    private BigDecimal trainingDuration;

    @ManyToOne
    @JoinColumn(name = "training_type_id")
    private TrainingType trainingType;

    @Override
    public String toString() {
        return "Training ID: " + id +
                "\nTraining Name: " + trainingName +
                "\nTraining Date: " + trainingDate +
                "\nDuration: " + trainingDuration +
                "\nTrainee ID: " + trainee.getId() +
                "\nUser ID: " + trainee.getUser().getId() + "\nTrainee: " + trainee.getUser().getFirstName() + " " + trainee.getUser().getLastName()
                + " USER ID :" + trainee.getUser().getId() +
                "\nTrainer: " + trainer.getUser().getFirstName() + " " + trainer.getUser().getLastName()
                + " USER ID :" + trainer.getUser().getId() +

                "\nTraining Type: " + trainingType.getTrainingTypeName();
    }

}
