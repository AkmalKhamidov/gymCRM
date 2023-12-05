package com.epamlearning.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

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
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE,
            org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.PERSIST})
    private User user;

    @ManyToOne
    @JoinColumn(name="specialization", referencedColumnName = "id")
    private TrainingType specialization;

    @Override
    public String toString() {
        return "Trainer{" +
                "id=" + id +
                ", user=" + user +
                ", specialization=" + specialization +
                '}';
    }
}
