package com.epamlearning.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// TODO: create attribute list<trainers>

@Entity
@Table(name = "trainees")
@NoArgsConstructor
@Getter
@Setter
public class Trainee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    @Column(name = "address")
    private String address;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @Cascade({CascadeType.PERSIST, CascadeType.REMOVE,
            CascadeType.MERGE, CascadeType.PERSIST})
    private User user;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "trainings",
            joinColumns = @JoinColumn(name = "trainee_id"),
            inverseJoinColumns = @JoinColumn(name = "trainer_id"))
    private List<Trainer> trainers;


    @Override
    public String toString() {
        return "Trainee{" +
                "id=" + id +
                ", dateOfBirth=" + dateOfBirth +
                ", address='" + address + '\'' +
                ", user=" + user +
                '}';
    }
}

