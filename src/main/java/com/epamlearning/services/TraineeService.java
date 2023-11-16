package com.epamlearning.services;

import com.epamlearning.daos.TraineeDAO;
import com.epamlearning.models.Trainee;
import com.epamlearning.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TraineeService {
    private TraineeDAO traineeDAO;

    @Autowired
    public void setTraineeDAO(TraineeDAO traineeDAO) {
        this.traineeDAO = traineeDAO;
    }

    public Trainee createTrainee(Trainee trainee) {
        trainee.setId(generateNewTraineeId());
        return traineeDAO.saveTrainee(trainee);
    }

    public Trainee updateTrainee(Trainee trainee) {
        return traineeDAO.updateTrainee(trainee);
    }

    public void deleteTrainee(Long id) {
        traineeDAO.deleteTraineeById(id);
    }

    public Trainee getTraineeById(Long id) {
        return traineeDAO.findTraineeById(id);
    }

    public List<Trainee> getAllTrainees() {
        return traineeDAO.findAllTrainees();
    }

    private long generateNewTraineeId() {
        long maxId = traineeDAO.findAllTrainees().stream()
                .mapToLong(Trainee::getId)
                .max()
                .orElse(0);
        return maxId + 1;
    }

}
