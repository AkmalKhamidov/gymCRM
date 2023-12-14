package com.epamlearning.facade;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

// TODO: separate facade for trainee and trainer

@Component
@Transactional
@Slf4j
public class GymCRMFacade implements Facade{

//    private final TraineeService traineeService;
//    private final TrainerService trainerService;
//    private final TrainingService trainingService;
//    private final UserService userService;

    private final SignUpFacade signUpFacade;
    private final LogInFacade logInFacade;

    Scanner sc = new Scanner(System.in);

    @Autowired
    public GymCRMFacade(SignUpFacade signUpFacade, LogInFacade logInFacade) {
        this.signUpFacade = signUpFacade;
        this.logInFacade = logInFacade;
    }

    @Override
    public void getPage() {
        System.out.flush();
        System.out.println("Welcome to GymCRM");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
        int choice = sc.nextInt();
        switch (choice) {
            case 1 -> logInFacade.getPage();
            case 2 -> signUpFacade.getPage();
            case 3 -> System.out.println("Thank you for using Gym CRM");
            default -> System.out.println("Invalid choice");
        }
    }
//    public Trainee createTrainee(String firstName, String lastName, String address, Date dateOfBirth) {
//        User user = userService.createUser(firstName, lastName);
//        return traineeService.save(traineeService.createTrainee(user, address, dateOfBirth)).get();
//    }
//
//    public Trainer createTrainer(String firstName, String lastName, TrainingType trainingType) {
//        User user = userService.createUser(firstName, lastName);
//        Trainer trainer = new Trainer();
//        trainer.setUser(user);
//        trainer.setSpecialization(trainingType);
//        return trainerService.save(trainer).get();
//    }
//
//    public Trainee updateTrainee(String username, String password, Long traineeId, Trainee trainee) {
//        if (traineeService.authenticate(username, password)) {
//            return traineeService.update(traineeId, trainee).get();
//        }
//        return null;
//    }
//
//    public Trainer updateTrainer(String username, String password, Long trainerId, Trainer trainer) {
//        if (trainerService.authenticate(username, password)) {
//            return trainerService.update(trainerId, trainer).get();
//        }
//        return null;
//    }
//
//    public boolean traineeUserNameAndPasswordMatching(String username, String password) {
//        return traineeService.authenticate(username, password);
//    }
//
//    public boolean trainerUserNameAndPasswordMatching(String username, String password) {
//        return trainerService.authenticate(username, password);
//    }
//
//    public Trainer getTrainerByUsername(String username, String password, String trainerUsername) {
//        if (userService.authenticate(username, password)) {
//            return trainerService.findByUsername(trainerUsername).get();
//        }
//        return null;
//    }
//
//    public Trainee getTraineeByUsername(String username, String password, String traineeUsername) {
//        if (userService.authenticate(username, password)) {
//            return traineeService.findByUsername(traineeUsername).get();
//        }
//        return null;
//    }
//
//    public Trainee changeTraineePassword(String username, String password, Long traineeId, String newPassword) {
//        if (traineeService.authenticate(username, password)) {
//            return traineeService.updatePassword(traineeId, newPassword).get();
//        }
//        return null;
//    }
//
//    public Trainer changeTrainerPassword(String username, String password, Long trainerId, String newPassword) {
//        if (trainerService.authenticate(username, password)) {
//            return trainerService.updatePassword(trainerId, newPassword).get();
//        }
//        return null;
//    }
//
//    // TODO: add parameter status boolean. remove method duplication
//    // TODO: catch exception for notAuthenticated and log it.
//
////    public Trainee activateTrainee(String username, String password, Long traineeId) {
////        if (traineeService.authenticate(username, password)) {
////            return traineeService.updateActive(traineeId, true).get();
////        }
////        return null;
////    }
//    // TODO: Ask from mentors:
//    //  1. Bidirectional relationship between trainee and trainer.
//    //  2. How to update list of trainers for trainee.
//    //  3. Ask about changeTraineeActive methods. Does try catch implemented correctly?
//    //  4. Ask about how to move queries into file/properties.
//
//    public Trainee changeTraineeActive(String username, String password, Long traineeId, boolean active) {
//        Trainee trainee = null;
//        try {
//            traineeService.authenticate(username, password);
//            trainee = traineeService.updateActive(traineeId, active).get();
//        } catch (NotAuthenticated | NullPointerException | NotFoundException e) {
//            log.warn("Error: {}", e.getMessage());
//        }
//        return trainee;
//    }
//
//
//    public Trainer changeTrainerActive(String username, String password, Long trainerId, boolean active) {
//        if (trainerService.authenticate(username, password)) {
//            return trainerService.updateActive(trainerId, active).get();
//        }
//        return null;
//    }
//
////    public Trainer activateTrainer(String username, String password, Long trainerId) {
////        if (trainerService.authenticate(username, password)) {
////            return trainerService.updateActive(trainerId, true).get();
////        }
////        return null;
////    }
////
////    public Trainer deactivateTrainer(String username, String password, Long trainerId) {
////        if (trainerService.authenticate(username, password)) {
////            return trainerService.updateActive(trainerId, false).get();
////        }
////        return null;
////    }
//
//    public void deleteTraineeByUsername(String username, String password, String usernameToDelete) {
//        if (traineeService.authenticate(username, password)) {
//            Trainee traineeToDelete = traineeService.findByUsername(username).get();
//            trainingService.deleteTrainingsByTrainee(traineeToDelete.getId());
//            traineeService.deleteById(traineeToDelete.getId());
//        }
//    }
//
//    public List<Training> findTrainingsByTraineeUsernameAndCriteria(String username, String password, String usernameToFind, Date dateFrom, Date dateTo, TrainingType trainingType) {
//        if (userService.authenticate(username, password)) {
//            Trainee trainee = traineeService.findByUsername(usernameToFind).get();
//            return trainingService.findByTraineeAndCriteria(trainee, dateFrom, dateTo, trainingType).stream().flatMap(Optional::stream).collect(Collectors.toList());
//        }
//        return null;
//    }
//
//    public List<Training> findTrainingsByTrainerUsernameAndCriteria(String username, String password, String usernameToFind, Date dateFrom, Date dateTo, TrainingType trainingType) {
//        if (userService.authenticate(username, password)) {
//            Trainer trainer = trainerService.findByUsername(usernameToFind).get();
//            return trainingService.findByTrainerAndCriteria(trainer, dateFrom, dateTo, trainingType).stream().flatMap(Optional::stream).collect(Collectors.toList());
//        }
//        return null;
//    }
//
//
//    public Training createTraining(String username, String password, String trainingName, Date trainingDate, BigDecimal trainingDuration, Trainer trainer, Trainee trainee, TrainingType trainingType) {
//        if (userService.authenticate(username, password)) {
//            Training training = new Training();
//            training.setTrainingName(trainingName);
//            training.setTrainingDate(trainingDate);
//            training.setTrainingDuration(trainingDuration);
//            training.setTrainer(trainer);
//            training.setTrainee(trainee);
//            training.setTrainingType(trainingType);
//            return trainingService.save(training).get();
//        }
//        return null;
//    }
//
//    public List<Trainer> findNotAssignedActiveTrainers(String username, String password, Trainee trainee) {
//        if (traineeService.authenticate(username, password)) {
//            return trainerService.findNotAssignedActiveTrainers(trainee).stream().flatMap(Optional::stream).collect(Collectors.toList());
//        }
//        return null;
//    }
//
//    public List<Trainer> updateTraineeTrainers(String username, String password, Long traineeId, List<Trainer> trainers) {
//        if (traineeService.authenticate(username, password)) {
//            Optional<Trainee> trainee = traineeService.findById(traineeId);
//            return trainingService.updateTraineeTrainers(trainee.get().getId(), trainers).stream().flatMap(Optional::stream).collect(Collectors.toList());
//        }
//        return null;
//    }

}
