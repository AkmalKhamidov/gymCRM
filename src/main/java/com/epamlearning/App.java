package com.epamlearning;

import com.epamlearning.configs.ApplicationConfig;
import com.epamlearning.facade.GymCRMFacade;
import com.epamlearning.models.Trainee;
import com.epamlearning.models.Trainer;
import com.epamlearning.models.Training;
import com.epamlearning.models.TrainingType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
public class App {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                ApplicationConfig.class
        );


        GymCRMFacade gymCRMFacade = context.getBean(GymCRMFacade.class);

        Date dateFrom = new Calendar.Builder().setDate(2023, Calendar.SEPTEMBER,15).build().getTime();
        Date dateTo = new Calendar.Builder().setDate(2024,Calendar.JANUARY,15).build().getTime();
        TrainingType trainingType = new TrainingType();
        trainingType.setId(1L);
        trainingType.setTrainingTypeName("GYM_TYPE");

        // Example: Create Trainee
        Trainee createdTrainee = gymCRMFacade.createTrainee("TraineeFirstName", "TraineeLastName", "TraineeAddress", new Calendar.Builder().setDate(2002, Calendar.JANUARY, 24).build().getTime());
        log.info("Trainee created: {}", createdTrainee);

        // Example: Create Trainer
        Trainer createdTrainer = gymCRMFacade.createTrainer("TrainerFirstName", "TrainerLastName", trainingType);
        log.info("Trainer created: {}", createdTrainer);


        String traineeUsername = createdTrainee.getUser().getUsername();
        String traineePassword = createdTrainee.getUser().getPassword();

        String trainerUsername = createdTrainer.getUser().getUsername();
        String trainerPassword = createdTrainer.getUser().getPassword();

        // Example: TraineeUserNameAndPasswordMatching
        boolean isTraineeUserNameAndPasswordMatching = gymCRMFacade.traineeUserNameAndPasswordMatching(traineeUsername, traineePassword);
        log.info("Trainee with id: {} username and password matching: {}", createdTrainee.getId(),  isTraineeUserNameAndPasswordMatching);

        // Example: TrainerUserNameAndPasswordMatching
        boolean isTrainerUserNameAndPasswordMatching = gymCRMFacade.trainerUserNameAndPasswordMatching(trainerUsername, trainerPassword);
        log.info("Trainer with id: {} username and password matching: {}", createdTrainer.getId(),  isTrainerUserNameAndPasswordMatching);

        // Example: getTraineeByUsername
        Trainee traineeByUsername = gymCRMFacade.getTraineeByUsername(traineeUsername, traineePassword, traineeUsername);
        log.info("Trainee found by username: {}", traineeByUsername);

        // Example: getTrainerByUsername
        Trainer trainerByUsername = gymCRMFacade.getTrainerByUsername(trainerUsername, trainerPassword, trainerUsername);
        log.info("Trainer found by username: {}", trainerByUsername);

        // Example: changeTraineePassword
        String oldTraineePassword = traineePassword;
        traineePassword = "newPassword";
        Trainee traineeWithChangedPassword = gymCRMFacade.changeTraineePassword(traineeUsername, oldTraineePassword, createdTrainee.getId(), traineePassword);
        log.info("Trainee changed password. Trainee: {}", traineeWithChangedPassword);

        // Example: changeTrainerPassword
        String oldTrainerPassword = trainerPassword;
        trainerPassword = "newPassword";
        Trainer trainerWithChangedPassword = gymCRMFacade.changeTrainerPassword(trainerUsername, oldTrainerPassword, createdTrainer.getId(), trainerPassword);
        log.info("Trainer changed password. Trainer: {}", trainerWithChangedPassword);

        // Example: Update Trainee
        traineeWithChangedPassword.setAddress("UPDATED TraineeAddress");
        Trainee updatedTrainee = gymCRMFacade.updateTrainee(traineeUsername, traineePassword, createdTrainee.getId(), traineeWithChangedPassword);
        log.info("Trainee updated: {}", updatedTrainee);

        // Example: Update Trainer
        TrainingType newTrainingType = new TrainingType();
        newTrainingType.setId(2L);
        newTrainingType.setTrainingTypeName("GYM_TYPE_2");
        trainerWithChangedPassword.setSpecialization(newTrainingType);
        Trainer updatedTrainer = gymCRMFacade.updateTrainer(trainerUsername, trainerPassword, createdTrainer.getId(), trainerWithChangedPassword);
        log.info("Trainer updated: {}", updatedTrainer);

        // Example: Find Trainings By Trainee Username and Criteria
        List<Training> trainingsByTrainee = gymCRMFacade.findTrainingsByTraineeUsernameAndCriteria(
                traineeUsername, traineePassword, traineeUsername, dateFrom, dateTo, trainingType);
        log.info("Trainings found for trainee {}: {}", traineeUsername, trainingsByTrainee);

        // Example: Find Trainings By Trainer Username and Criteria
        List<Training> trainingsByTrainer = gymCRMFacade.findTrainingsByTrainerUsernameAndCriteria(
                trainerUsername, trainerPassword, trainerUsername, dateFrom, dateTo, trainingType);
        log.info("Trainings found for trainer {}: {}", trainerUsername, trainingsByTrainer);

        // Example: Deactivate Trainee
        Trainee deactivatedTrainee = gymCRMFacade.deactivateTrainee(traineeUsername, traineePassword, updatedTrainee.getId());
        log.info("Trainee deactivated: {}", deactivatedTrainee);

        // Example: Activate Trainee
        Trainee activatedTrainee = gymCRMFacade.activateTrainee(traineeUsername, traineePassword, updatedTrainee.getId());
        log.info("Trainee activated: {}", activatedTrainee);

        // Example: Deactivate Trainer
        Trainer deactivatedTrainer = gymCRMFacade.deactivateTrainer(trainerUsername, trainerPassword, updatedTrainer.getId());
        log.info("Trainer deactivated: {}", deactivatedTrainer);

        // Example: Activate Trainer
        Trainer activatedTrainer = gymCRMFacade.activateTrainer(trainerUsername, trainerPassword, updatedTrainer.getId());
        log.info("Trainer activated: {}", activatedTrainer);

        // Example: Create Trainer2
        Trainer createdTrainer2 = gymCRMFacade.createTrainer("Trainer2FirstName", "Trainer2LastName", trainingType);
        log.info("Trainer2 created: {}", createdTrainer2);

        // Example: Find Not Assigned Active Trainers
        List<Trainer> notAssignedActiveTrainers = gymCRMFacade.findNotAssignedActiveTrainers(traineeUsername, traineePassword, activatedTrainee);
        log.info("Not assigned active trainers for trainee {}: {}", createdTrainee.getUser().getUsername(), notAssignedActiveTrainers);

        // Example: Create Training
        String trainingName = "New Training";
        Date trainingDate = new Calendar.Builder().setDate(2023, Calendar.DECEMBER,4).build().getTime();
        BigDecimal trainingDuration = BigDecimal.valueOf(15);
        Training createdTraining = gymCRMFacade.createTraining(trainerUsername, trainerPassword, trainingName, trainingDate, trainingDuration, activatedTrainer, activatedTrainee, trainingType);
        log.info("Created training: {}", createdTraining);

        // Example: Update trainee trainers
        List<Trainer> newTrainers = List.of(createdTrainer2);
        List<Trainer> updatedTrainers = gymCRMFacade.updateTraineeTrainers(traineeUsername, traineePassword, activatedTrainee.getId(), newTrainers);
        log.info("Updated trainers for trainee {}: {}", createdTrainee.getUser().getUsername(), updatedTrainers);

        // Example: Delete Trainee By Username
        gymCRMFacade.deleteTraineeByUsername(traineeUsername, traineePassword, traineeUsername);
        log.info("Trainee with username {} deleted", traineeUsername);
    }
}
