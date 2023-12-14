package com.epamlearning.facade;

import com.epamlearning.models.*;
import com.epamlearning.services.TraineeService;
import com.epamlearning.services.TrainerService;
import com.epamlearning.services.TrainingService;
import com.epamlearning.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class GymCRMFacadeTest {

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @Mock
    private TrainingService trainingService;

    @Mock
    private UserService userService;

    @InjectMocks
    private GymCRMFacade gymCRMFacade;

//    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
//        user = new User();
//        user.setId(1L);
//        user.setUsername("TestUsername");
//        user.setFirstName("TestFirstName");
//        user.setLastName("TestLastName");
//        user.setActive(true);
    }

    @Test
    void createTrainee() {
        // Mock data
        String firstName = "John";
        String lastName = "Doe";
        String address = "123 Main St";
        Date dateOfBirth = new Date();

        // Mock interactions
        when(userService.createUser(firstName, lastName)).thenReturn(new User());
        when(traineeService.save(any())).thenReturn(Optional.of(new Trainee()));

        // Perform the method call
        Trainee result = gymCRMFacade.createTrainee(firstName, lastName, address, dateOfBirth);

        // Verify interactions and assertions
        verify(userService).createUser(firstName, lastName);
        verify(traineeService).save(any());
        assertNotNull(result);
    }

    @Test
    void createTrainer() {
        // Mock data
        String firstName = "TestFirstName";
        String lastName = "TestLastName";
        TrainingType trainingType = new TrainingType();
        trainingType.setId(1L);

        // Mock interactions
        when(userService.createUser(firstName, lastName)).thenReturn(new User());

        when(trainerService.save(any())).thenReturn(Optional.of(new Trainer()));

        // Perform the method call
        Trainer result = gymCRMFacade.createTrainer(firstName, lastName, trainingType);

        // Verify interactions and assertions
        verify(userService).createUser(firstName, lastName);
        verify(trainerService).save(any());
        assertNotNull(result);
    }

    @Test
    void updateTrainee() {
        // Mock data
        String username = "john_doe";
        String password = "password";
        Long traineeId = 1L;
        Trainee trainee = new Trainee();

        // Mock interactions
        when(traineeService.authenticate(username, password)).thenReturn(true);
        when(traineeService.update(traineeId, trainee)).thenReturn(Optional.of(new Trainee()));

        // Perform the method call
        Trainee result = gymCRMFacade.updateTrainee(username, password, traineeId, trainee);

        // Verify interactions and assertions
        verify(traineeService).authenticate(username, password);
        verify(traineeService).update(traineeId, trainee);
        assertNotNull(result);
    }

    @Test
    void updateTrainer() {
        // Mock data
        String username = "jane_smith";
        String password = "password";
        Long trainerId = 2L;
        Trainer trainer = new Trainer();

        // Mock interactions
        when(trainerService.authenticate(username, password)).thenReturn(true);
        when(trainerService.update(trainerId, trainer)).thenReturn(Optional.of(new Trainer()));

        // Perform the method call
        Trainer result = gymCRMFacade.updateTrainer(username, password, trainerId, trainer);

        // Verify interactions and assertions
        verify(trainerService).authenticate(username, password);
        verify(trainerService).update(trainerId, trainer);
        assertNotNull(result);
    }

    @Test
    void traineeUserNameAndPasswordMatching() {
        // Mock data
        String username = "john_doe";
        String password = "password";

        // Mock interactions
        when(traineeService.authenticate(username, password)).thenReturn(true);

        // Perform the method call
        boolean result = gymCRMFacade.traineeUserNameAndPasswordMatching(username, password);

        // Verify interactions and assertions
        verify(traineeService).authenticate(username, password);
        assertTrue(result);
    }

    @Test
    void trainerUserNameAndPasswordMatching() {
        // Mock data
        String username = "jane_smith";
        String password = "password";

        // Mock interactions
        when(trainerService.authenticate(username, password)).thenReturn(true);

        // Perform the method call
        boolean result = gymCRMFacade.trainerUserNameAndPasswordMatching(username, password);

        // Verify interactions and assertions
        verify(trainerService).authenticate(username, password);
        assertTrue(result);
    }

    @Test
    void getTrainerByUsername() {
        // Mock data
        String mainUsername = "admin";
        String mainPassword = "admin123";
        String trainerUsername = "jane_smith";

        // Mock interactions
        when(userService.authenticate(mainUsername, mainPassword)).thenReturn(true);
        when(trainerService.findByUsername(trainerUsername)).thenReturn(Optional.of(new Trainer()));

        // Perform the method call
        Trainer result = gymCRMFacade.getTrainerByUsername(mainUsername, mainPassword, trainerUsername);

        // Verify interactions and assertions
        verify(userService).authenticate(mainUsername, mainPassword);
        verify(trainerService).findByUsername(trainerUsername);
        assertNotNull(result);
    }

    @Test
    void getTraineeByUsername() {
        // Mock data
        String mainUsername = "admin";
        String mainPassword = "admin123";
        String traineeUsername = "john_doe";

        // Mock interactions
        when(userService.authenticate(mainUsername, mainPassword)).thenReturn(true);
        when(traineeService.findByUsername(traineeUsername)).thenReturn(Optional.of(new Trainee()));

        // Perform the method call
        Trainee result = gymCRMFacade.getTraineeByUsername(mainUsername, mainPassword, traineeUsername);

        // Verify interactions and assertions
        verify(userService).authenticate(mainUsername, mainPassword);
        verify(traineeService).findByUsername(traineeUsername);
        assertNotNull(result);
    }

    @Test
    void changeTraineePassword() {
        // Mock data
        String username = "john_doe";
        String password = "old_password";
        Long traineeId = 1L;
        String newPassword = "new_password";
        Trainee trainee = new Trainee();

        // Mock interactions
        when(traineeService.authenticate(username, password)).thenReturn(true);
        when(traineeService.updatePassword(traineeId, newPassword)).thenReturn(Optional.of(new Trainee()));

        // Perform the method call
        Trainee result = gymCRMFacade.changeTraineePassword(username, password, traineeId, newPassword);

        // Verify interactions and assertions
        verify(traineeService).authenticate(username, password);
        verify(traineeService).updatePassword(traineeId, newPassword);
        assertNotNull(result);
    }

    @Test
    void changeTrainerPassword() {
        // Mock data
        String username = "jane_smith";
        String password = "old_password";
        Long trainerId = 1L;
        String newPassword = "new_password";
        Trainer trainer = new Trainer();
//        trainer.getUser().setPassword(newPassword);
        // Mock interactions
        when(trainerService.authenticate(username, password)).thenReturn(true);
        when(trainerService.updatePassword(trainerId, newPassword)).thenReturn(Optional.of(new Trainer()));

        // Perform the method call
        Trainer result = gymCRMFacade.changeTrainerPassword(username, password, trainerId, newPassword);

        // Verify interactions and assertions
        verify(trainerService).authenticate(username, password);
        verify(trainerService).updatePassword(trainerId, newPassword);
        assertNotNull(result);
    }

    @Test
    void activateTrainee() {
        // Mock data
        String username = "jane_smith";
        String password = "password";
        Long traineeId = 1L;

        // Mock interactions
        when(traineeService.authenticate(username, password)).thenReturn(true);
        when(traineeService.updateActive(traineeId, true)).thenReturn(Optional.of(new Trainee()));

        // Perform the method call
        Trainee result = gymCRMFacade.activateTrainee(username, password, traineeId);

        // Verify interactions and assertions
        verify(traineeService).authenticate(username, password);
        verify(traineeService).updateActive(traineeId, true);
        assertNotNull(result);
    }

    @Test
    void deactivateTrainee() {
        // Mock data
        String username = "jane_smith";
        String password = "password";
        Long traineeId = 1L;

        // Mock interactions
        when(traineeService.authenticate(username, password)).thenReturn(true);
        when(traineeService.updateActive(traineeId, false)).thenReturn(Optional.of(new Trainee()));

        // Perform the method call
        Trainee result = gymCRMFacade.deactivateTrainee(username, password, traineeId);

        // Verify interactions and assertions
        verify(traineeService).authenticate(username, password);
        verify(traineeService).updateActive(traineeId, false);
        assertNotNull(result);
    }

    @Test
    void activateTrainer() {
        // Mock data
        String username = "john_doe";
        String password = "password";
        Long trainerId = 1L;

        // Mock interactions
        when(trainerService.authenticate(username, password)).thenReturn(true);
        when(trainerService.updateActive(trainerId, true)).thenReturn(Optional.of(new Trainer()));

        // Perform the method call
        Trainer result = gymCRMFacade.activateTrainer(username, password, trainerId);

        // Verify interactions and assertions
        verify(trainerService).authenticate(username, password);
        verify(trainerService).updateActive(trainerId, true);
        assertNotNull(result);
    }

    @Test
    void deactivateTrainer() {
        // Mock data
        String username = "john_doe";
        String password = "password";
        Long trainerId = 1L;

        // Mock interactions
        when(trainerService.authenticate(username, password)).thenReturn(true);
        when(trainerService.updateActive(trainerId, false)).thenReturn(Optional.of(new Trainer()));

        // Perform the method call
        Trainer result = gymCRMFacade.deactivateTrainer(username, password, trainerId);

        // Verify interactions and assertions
        verify(trainerService).authenticate(username, password);
        verify(trainerService).updateActive(trainerId, false);
        assertNotNull(result);
    }

    @Test
    void deleteTraineeByUsername() {
        // Mock data
        String username = "jane_smith";
        String password = "password";

        // Mock interactions
        when(traineeService.authenticate(username, password)).thenReturn(true);
        Trainee trainee = new Trainee();
        trainee.setId(1L); // Set a specific ID for the trainee
        when(traineeService.findByUsername(username)).thenReturn(Optional.of(trainee));
        doNothing().when(traineeService).deleteById(anyLong());

        // Perform the method call
        gymCRMFacade.deleteTraineeByUsername(username, password, username);

        // Verify interactions and assertions
        verify(traineeService).authenticate(username, password);
        verify(traineeService).findByUsername(username);
        verify(traineeService).deleteById(anyLong()); // Verify that deleteById is called with any long value
    }

    @Test
    void createTraining() {
        // Mock data
        String mainUsername = "admin";
        String mainPassword = "admin123";
        String trainingName = "Strength Training";
        BigDecimal duration = new BigDecimal("50.00");
        Date trainingDate = new Date();
        Trainee trainee = new Trainee();
        trainee.setId(1L);
        Trainer trainer = new Trainer();
        trainer.setId(1L);
        TrainingType trainingType = new TrainingType();
        trainingType.setId(1L);

        // Mock interactions
        when(userService.authenticate(mainUsername, mainPassword)).thenReturn(true);
        when(trainingService.save(any())).thenReturn(Optional.of(new Training()));

        // Perform the method call
        Training result = gymCRMFacade.createTraining(mainUsername, mainPassword, trainingName, trainingDate, duration, trainer, trainee, trainingType);

        // Verify interactions and assertions
        verify(userService).authenticate(mainUsername, mainPassword);
        verify(trainingService).save(any());
        assertNotNull(result);
    }

    @Test
    void findTrainingsByTraineeUsernameAndCriteria() {
        // Mock data
        String username = "admin";
        String password = "admin_password";
        String traineeUsername = "trainee_user";
        Date dateFrom = new Date();
        Date dateTo = new Date();
        TrainingType trainingType = new TrainingType();
        trainingType.setId(1L);
        List<Optional<Training>> trainings = new ArrayList<>();

        // Mock interactions
        when(userService.authenticate(username, password)).thenReturn(true);
        Trainee trainee = new Trainee();
        when(traineeService.findByUsername(traineeUsername)).thenReturn(Optional.of(trainee));
        when(trainingService.findByTraineeAndCriteria(trainee, dateFrom, dateTo, trainingType)).thenReturn(trainings);

        // Perform the method call
        List<Training> result = gymCRMFacade.findTrainingsByTraineeUsernameAndCriteria(username, password, traineeUsername, dateFrom, dateTo, trainingType);

        // Verify interactions and assertions
        verify(userService).authenticate(username, password);
        verify(traineeService).findByUsername(traineeUsername);
        verify(trainingService).findByTraineeAndCriteria(trainee, dateFrom, dateTo, trainingType);
        assertNotNull(result);
    }

    @Test
    void findTrainingsByTrainerUsernameAndCriteria() {
        // Mock data
        String username = "admin";
        String password = "admin_password";
        String trainerUsername = "trainer_user";
        Date dateFrom = new Date();
        Date dateTo = new Date();
        TrainingType trainingType = new TrainingType();
        trainingType.setId(1L);
        List<Optional<Training>> trainings = new ArrayList<>();

        // Mock interactions
        when(userService.authenticate(username, password)).thenReturn(true);
        Trainer trainer = new Trainer();
        when(trainerService.findByUsername(trainerUsername)).thenReturn(Optional.of(trainer));
        when(trainingService.findByTrainerAndCriteria(trainer, dateFrom, dateTo, trainingType)).thenReturn(trainings);

        // Perform the method call
        List<Training> result = gymCRMFacade.findTrainingsByTrainerUsernameAndCriteria(username, password, trainerUsername, dateFrom, dateTo, trainingType);

        // Verify interactions and assertions
        verify(userService).authenticate(username, password);
        verify(trainerService).findByUsername(trainerUsername);
        verify(trainingService).findByTrainerAndCriteria(trainer, dateFrom, dateTo, trainingType);
        assertNotNull(result);
    }

    @Test
    void findNotAssignedActiveTrainers() {
        // Mock data
        String username = "admin";
        String password = "admin_password";
        Trainee trainee = new Trainee();
        List<Optional<Trainer>> trainers = new ArrayList<>();
        // Mock interactions
        when(traineeService.authenticate(username, password)).thenReturn(true);
        when(trainerService.findNotAssignedActiveTrainers(trainee)).thenReturn(trainers);

        // Perform the method call
        List<Trainer> result = gymCRMFacade.findNotAssignedActiveTrainers(username, password, trainee);

        // Verify interactions and assertions
        verify(traineeService).authenticate(username, password);
        verify(trainerService).findNotAssignedActiveTrainers(trainee);
        assertNotNull(result);
    }

    @Test
    void updateTraineeTrainers() {
        // Mock data
        String username = "admin";
        String password = "admin_password";
        Long traineeId = 1L;
        Trainee trainee = new Trainee();
        trainee.setId(traineeId);
        Trainer trainer1 = new Trainer();
        Trainer trainer2 = new Trainer();
        List<Optional<Trainer>> oldTrainers = new ArrayList<>();
        oldTrainers.add(Optional.of(trainer1));
        List<Trainer> trainers = Arrays.asList(trainer2);

        // Mock interactions
        when(traineeService.authenticate(username, password)).thenReturn(true);
        when(traineeService.findById(traineeId)).thenReturn(Optional.of(trainee));
        when(trainingService.updateTraineeTrainers(eq(traineeId), anyList())).thenReturn(oldTrainers);

        // Perform the method call
        List<Trainer> result = gymCRMFacade.updateTraineeTrainers(username, password, traineeId, trainers);

        // Verify interactions and assertions
        verify(traineeService).authenticate(username, password);
        verify(traineeService).findById(traineeId);
        verify(trainingService).updateTraineeTrainers(eq(traineeId), anyList());
        assertNotNull(result);
    }
}
