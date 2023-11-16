package com.epamlearning.daos;

import com.epamlearning.models.Training;

import java.util.List;

public interface TrainingDAO {
    Training saveTraining(Training training);
    Training updateTraining (Training training);
    Training findTrainingById(Long id);
    List<Training> findAllTrainings();
    void deleteTrainingById(Long id);
}
