package com.epamlearning.daos;

import com.epamlearning.models.Trainee;

import java.util.List;

public interface TraineeDAO {
    // crud functions to manipulate data
    Trainee saveTrainee(Trainee trainee);

    Trainee updateTrainee(Trainee trainee);

    Trainee findTraineeById(Long id);

    List<Trainee> findAllTrainees();

    void deleteTraineeById(Long id);
}
