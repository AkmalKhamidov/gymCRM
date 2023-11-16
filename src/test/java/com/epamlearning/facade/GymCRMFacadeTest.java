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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);
        gymCRMFacade = new GymCRMFacade(traineeService, trainerService, trainingService, userService);
    }

    @Test
    void createTrainee() {
        User user = new User();
        Trainee trainee = new Trainee();
        trainee.setUser(user);
        when(userService.createUser("John", "Doe")).thenReturn(user);
        when(traineeService.createTrainee(any())).thenReturn(trainee);

        Trainee craetedTrainee = gymCRMFacade.createTrainee("John", "Doe", "Address", new Date());

        assertNotNull(craetedTrainee);
        assertNotNull(craetedTrainee.getUser());
        verify(userService, times(1)).createUser("John", "Doe");
        verify(traineeService, times(1)).createTrainee(any());
    }

    @Test
    void createTrainer() {
        User user = new User();
        Trainer trainer = new Trainer();
        TrainingType trainingType = new TrainingType();
        trainer.setSpecialization(trainingType);
        trainer.setUser(user);

        when(userService.createUser("Jane", "Smith")).thenReturn(user);
        when(trainerService.createTrainer(any())).thenReturn(trainer);

        Trainer createdTrainer = gymCRMFacade.createTrainer("Jane", "Smith", "TrainingType");

        assertNotNull(createdTrainer);
        assertNotNull(createdTrainer.getUser());
        assertNotNull(createdTrainer.getSpecialization());
        verify(userService, times(1)).createUser("Jane", "Smith");
        verify(trainerService, times(1)).createTrainer(any());
    }

    @Test
    void createTraining() {
        Trainer trainer = new Trainer();
        Trainee trainee = new Trainee();
        Training training = new Training();
        TrainingType trainingType = new TrainingType();
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingType(trainingType);

        when(traineeService.getTraineeById(1L)).thenReturn(trainee);
        when(trainerService.getTrainerById(2L)).thenReturn(trainer);
        when(trainingService.createTraining(any())).thenReturn(training);

        Training createdTraining = gymCRMFacade.createTraining(new Trainee(), new Trainer(), new TrainingType(), new Date(), BigDecimal.TEN);

        assertNotNull(createdTraining);
        assertNotNull(createdTraining.getTrainee());
        assertNotNull(createdTraining.getTrainer());
        assertNotNull(createdTraining.getTrainingType());
        verify(trainingService, times(1)).createTraining(any());
    }

    @Test
    void updateTrainee() {
        // Mocking
        when(traineeService.updateTrainee(any())).thenReturn(new Trainee());

        // Test
        Trainee trainee = gymCRMFacade.updateTrainee(new Trainee());

        // Assertions
        assertNotNull(trainee);
        verify(traineeService, times(1)).updateTrainee(any());
    }

    @Test
    void updateTrainer() {
        // Mocking
        when(trainerService.updateTrainer(any())).thenReturn(new Trainer());

        // Test
        Trainer trainer = gymCRMFacade.updateTrainer(new Trainer());

        // Assertions
        assertNotNull(trainer);
        verify(trainerService, times(1)).updateTrainer(any());
    }

    @Test
    void deleteTrainee() {
        Trainee trainee = new Trainee();
        trainee.setId(1L);
        doNothing().when(traineeService).deleteTrainee(1L);

        assertDoesNotThrow(() -> gymCRMFacade.deleteTrainee(trainee.getId()));

        verify(traineeService, times(1)).deleteTrainee(1L);
    }

    @Test
    void getAllTrainees() {
        // Mocking
        List<Trainee> trainees = new ArrayList<>();
        when(traineeService.getAllTrainees()).thenReturn(trainees);

        // Test
        List<Trainee> result = gymCRMFacade.getAllTrainees();

        // Assertions
        assertNotNull(result);
        assertEquals(trainees, result);
        verify(traineeService, times(1)).getAllTrainees();
    }

    @Test
    void getAllTrainers() {
        // Mocking
        List<Trainer> trainers = new ArrayList<>();
        when(trainerService.getAllTrainers()).thenReturn(trainers);

        // Test
        List<Trainer> result = gymCRMFacade.getAllTrainers();

        // Assertions
        assertNotNull(result);
        assertEquals(trainers, result);
        verify(trainerService, times(1)).getAllTrainers();
    }

    @Test
    void getAllTrainings() {
        // Mocking
        List<Training> trainings = new ArrayList<>();
        when(trainingService.getAllTrainings()).thenReturn(trainings);

        // Test
        List<Training> result = gymCRMFacade.getAllTrainings();

        // Assertions
        assertNotNull(result);
        assertEquals(trainings, result);
        verify(trainingService, times(1)).getAllTrainings();
    }
}
