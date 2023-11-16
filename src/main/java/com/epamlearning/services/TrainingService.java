package com.epamlearning.services;

import com.epamlearning.daos.TrainingDAO;
import com.epamlearning.models.Trainer;
import com.epamlearning.models.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingService {

    private TrainingDAO trainingDAO;

    @Autowired
    public void setTrainingDAO(TrainingDAO trainingDAO) {
        this.trainingDAO = trainingDAO;
    }

    public Training createTraining(Training training) {
        training.setId(generateNewTrainingId());
        return trainingDAO.saveTraining(training);
    }

//    public Training updateTraining(Training training){
//        return trainingDAO.updateTraining(training);
//    }

    public Training getTrainingById(Long id) {
        return trainingDAO.findTrainingById(id);
    }

    public List<Training> getAllTrainings(){
        return trainingDAO.findAllTrainings();
    }

    private long generateNewTrainingId() {
        long maxId = trainingDAO.findAllTrainings().stream()
                .mapToLong(Training::getId)
                .max()
                .orElse(0);
        return maxId + 1;
    }

}
