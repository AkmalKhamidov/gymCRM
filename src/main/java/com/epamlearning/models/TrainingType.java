package com.epamlearning.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrainingType that = (TrainingType) o;
        return Objects.equals(id, that.id) && Objects.equals(trainingTypeName, that.trainingTypeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, trainingTypeName);
    }
}
