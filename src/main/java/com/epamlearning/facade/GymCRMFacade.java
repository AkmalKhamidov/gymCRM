package com.epamlearning.facade;

import com.epamlearning.models.*;
import com.epamlearning.services.TraineeService;
import com.epamlearning.services.TrainerService;
import com.epamlearning.services.TrainingService;
import com.epamlearning.services.UserService;
import com.epamlearning.storage.InMemoryStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Component
public class GymCRMFacade {

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;
    private final UserService userService;

    @Autowired
    public GymCRMFacade(TraineeService traineeService, TrainerService trainerService, TrainingService trainingService, UserService userService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
        this.userService = userService;
    }

    public Trainee createTrainee(String firstName, String lastName, String address, Date dateOfBirth) {
        User user = userService.createUser(firstName, lastName);
        Trainee trainee = new Trainee();
        trainee.setUser(user);
        trainee.setAddress(address);
        trainee.setDateOfBirth(dateOfBirth);
        return traineeService.createTrainee(trainee);
    }

    public Trainer createTrainer(String firstName, String lastName, String trainingTypeName) {
        User user = userService.createUser(firstName, lastName);
        Trainer trainer = new Trainer();
        trainer.setUser(user);
        TrainingType trainingType = new TrainingType();
        trainingType.setTrainingTypeName(trainingTypeName);
        trainer.setSpecialization(trainingType);
        return trainerService.createTrainer(trainer);
    }

    public Training createTraining(Trainee trainee, Trainer trainer, TrainingType trainingType, Date date, BigDecimal duration) {
        Training training = new Training();
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingType(trainingType);
        training.setTrainingDate(date);
        training.setTrainingDuration(duration);
        return trainingService.createTraining(training);
    }

    public Trainee updateTrainee(Trainee trainee) {
        return traineeService.updateTrainee(trainee);
    }

    public Trainer updateTrainer(Trainer trainer) {
        return trainerService.updateTrainer(trainer);
    }

    public void deleteTrainee(Long traineeId) {
        traineeService.deleteTrainee(traineeId);
    }

    public List<Trainee> getAllTrainees() {
        return traineeService.getAllTrainees();
    }

    public List<Trainer> getAllTrainers() {
        return trainerService.getAllTrainers();
    }

    public List<Training> getAllTrainings() {
        return trainingService.getAllTrainings();
    }

}
