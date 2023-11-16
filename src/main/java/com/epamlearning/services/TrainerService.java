package com.epamlearning.services;

import com.epamlearning.daos.TrainerDAO;
import com.epamlearning.models.Trainee;
import com.epamlearning.models.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainerService {
    private TrainerDAO trainerDAO;

    @Autowired
    public void setTrainerDAO(TrainerDAO trainerDAO) {
        this.trainerDAO = trainerDAO;
    }

    public Trainer createTrainer(Trainer trainer) {
        trainer.setId(generateNewTrainerId());
        return trainerDAO.saveTrainer(trainer);
    }

    public Trainer updateTrainer(Trainer trainer){
        return trainerDAO.updateTrainer(trainer);
    }

    public Trainer getTrainerById(Long id) {
        return trainerDAO.findTrainerById(id);
    }

    public List<Trainer> getAllTrainers(){
        return trainerDAO.findAllTrainers();
    }

    private long generateNewTrainerId() {
        long maxId = trainerDAO.findAllTrainers().stream()
                .mapToLong(Trainer::getId)
                .max()
                .orElse(0);
        return maxId + 1;
    }

}
