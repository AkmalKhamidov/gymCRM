package com.epamlearning.facade;

import com.epamlearning.models.*;
import com.epamlearning.services.TraineeService;
import com.epamlearning.services.TrainerService;
import com.epamlearning.services.TrainingService;
import com.epamlearning.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Transactional
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
        trainee.setAddress(address);
        trainee.setDateOfBirth(dateOfBirth);
        trainee.setUser(user);
        return traineeService.save(trainee).get();
    }

    public Trainer createTrainer(String firstName, String lastName, TrainingType trainingType) {
        User user = userService.createUser(firstName, lastName);
        Trainer trainer = new Trainer();
        trainer.setUser(user);
        trainer.setSpecialization(trainingType);
        return trainerService.save(trainer).get();
    }

    public Trainee updateTrainee(String username, String password, Long traineeId, Trainee trainee) {
        if (traineeService.authenticate(username, password)) {
            return traineeService.update(traineeId, trainee).get();
        }
        return null;
    }

    public Trainer updateTrainer(String username, String password, Long trainerId, Trainer trainer) {
        if (trainerService.authenticate(username, password)) {
            return trainerService.update(trainerId, trainer).get();
        }
        return null;
    }

    public boolean traineeUserNameAndPasswordMatching(String username, String password) {
        return traineeService.authenticate(username, password);
    }

    public boolean trainerUserNameAndPasswordMatching(String username, String password) {
        return trainerService.authenticate(username, password);
    }

    public Trainer getTrainerByUsername(String username, String password, String trainerUsername) {
        if (userService.authenticate(username, password)) {
            return trainerService.findByUsername(trainerUsername).get();
        }
        return null;
    }

    public Trainee getTraineeByUsername(String username, String password, String traineeUsername) {
        if (userService.authenticate(username, password)) {
            return traineeService.findByUsername(traineeUsername).get();
        }
        return null;
    }

    public Trainee changeTraineePassword(String username, String password, Long traineeId, String newPassword) {
        if (traineeService.authenticate(username, password)) {
            return traineeService.updatePassword(traineeId, newPassword).get();
        }
        return null;
    }

    public Trainer changeTrainerPassword(String username, String password, Long trainerId, String newPassword) {
        if (trainerService.authenticate(username, password)) {
            return trainerService.updatePassword(trainerId, newPassword).get();
        }
        return null;
    }

    public Trainee activateTrainee(String username, String password, Long traineeId) {
        if (traineeService.authenticate(username, password)) {
            return traineeService.updateActive(traineeId, true).get();
        }
        return null;
    }

    public Trainee deactivateTrainee(String username, String password, Long traineeId) {
        if (traineeService.authenticate(username, password)) {
            return traineeService.updateActive(traineeId, false).get();
        }
        return null;
    }

    public Trainer activateTrainer(String username, String password, Long trainerId) {
        if (trainerService.authenticate(username, password)) {
            return trainerService.updateActive(trainerId, true).get();
        }
        return null;
    }

    public Trainer deactivateTrainer(String username, String password, Long trainerId) {
        if (trainerService.authenticate(username, password)) {
            return trainerService.updateActive(trainerId, false).get();
        }
        return null;
    }

    public void deleteTraineeByUsername(String username, String password, String usernameToDelete) {
        if (traineeService.authenticate(username, password)) {
            Trainee traineeToDelete = traineeService.findByUsername(username).get();
            trainingService.deleteTrainingsByTrainee(traineeToDelete.getId());
            traineeService.deleteById(traineeToDelete.getId());
        }
    }

    public List<Training> findTrainingsByTraineeUsernameAndCriteria(String username, String password, String usernameToFind, Date dateFrom, Date dateTo, TrainingType trainingType) {
        if (userService.authenticate(username, password)) {
            Trainee trainee = traineeService.findByUsername(usernameToFind).get();
            return trainingService.findByTraineeAndCriteria(trainee, dateFrom, dateTo, trainingType).stream().flatMap(Optional::stream).collect(Collectors.toList());
        }
        return null;
    }

    public List<Training> findTrainingsByTrainerUsernameAndCriteria(String username, String password, String usernameToFind, Date dateFrom, Date dateTo, TrainingType trainingType) {
        if (userService.authenticate(username, password)) {
            Trainer trainer = trainerService.findByUsername(usernameToFind).get();
            return trainingService.findByTrainerAndCriteria(trainer, dateFrom, dateTo, trainingType).stream().flatMap(Optional::stream).collect(Collectors.toList());
        }
        return null;
    }


    public Training createTraining(String username, String password, String trainingName, Date trainingDate, BigDecimal trainingDuration, Trainer trainer, Trainee trainee, TrainingType trainingType) {
        if (userService.authenticate(username, password)) {
            Training training = new Training();
            training.setTrainingName(trainingName);
            training.setTrainingDate(trainingDate);
            training.setTrainingDuration(trainingDuration);
            training.setTrainer(trainer);
            training.setTrainee(trainee);
            training.setTrainingType(trainingType);
            return trainingService.save(training).get();
        }
        return null;
    }

    public List<Trainer> findNotAssignedActiveTrainers(String username, String password, Trainee trainee) {
        if (traineeService.authenticate(username, password)) {
            return trainerService.findNotAssignedActiveTrainers(trainee).stream().flatMap(Optional::stream).collect(Collectors.toList());
        }
        return null;
    }

    public List<Trainer> updateTraineeTrainers(String username, String password, Long traineeId, List<Trainer> trainers) {
        if (traineeService.authenticate(username, password)) {
            Optional<Trainee> trainee = traineeService.findById(traineeId);
            return trainingService.updateTraineeTrainers(trainee.get().getId(), trainers).stream().flatMap(Optional::stream).collect(Collectors.toList());
        }
        return null;
    }

}
