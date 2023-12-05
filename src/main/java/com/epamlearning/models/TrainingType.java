package com.epamlearning.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "training_types")
@NoArgsConstructor
@Getter
@Setter
public class TrainingType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    @Column(name = "training_type_name",nullable = false)
    private String trainingTypeName;

    @Override
    public String toString() {
        return "TrainingType{" +
                "id=" + id +
                ", trainingTypeName='" + trainingTypeName + '\'' +
                '}';
    }
}
