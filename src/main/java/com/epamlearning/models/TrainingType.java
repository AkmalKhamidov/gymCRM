package com.epamlearning.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
public class TrainingType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String trainingTypeName;

    @Override
    public String toString() {
        return "Type ID: " + id +
                "\nTraining Type: " + trainingTypeName;
    }

}
