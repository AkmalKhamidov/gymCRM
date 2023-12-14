package com.epamlearning.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.ArrayList;
import java.util.List;

// TODO: create attribute list<trainees>


@Entity
@Table(name = "trainers")
@NoArgsConstructor
@Getter
@Setter
public class Trainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @Cascade({CascadeType.REMOVE, CascadeType.MERGE, CascadeType.PERSIST})
    private User user;

    @ManyToOne
    @JoinColumn(name="specialization", referencedColumnName = "id")
    private TrainingType specialization;

    @ManyToMany(mappedBy = "trainers")
    private List<Trainee> traineeList = new ArrayList<>();

    @Override
    public String toString() {
        return "Trainer{" +
                "id=" + id +
                ", user=" + user +
                ", specialization=" + specialization +
                '}';
    }
}
