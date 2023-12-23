package com.epamlearning.repositories;

import com.epamlearning.models.Trainee;
import com.epamlearning.models.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TraineeRepository  extends JpaRepository<Trainee, Long>, BaseRepository{
    Optional<Trainee> findByUsername(String username);

    List<Optional<Trainer>> findTrainersByTrainee(Trainee trainee);
}
