package com.epamlearning.services;

import com.epamlearning.daos.TrainingDAOImpl;
import com.epamlearning.exceptions.NotFoundException;
import com.epamlearning.models.Trainee;
import com.epamlearning.models.Trainer;
import com.epamlearning.models.Training;
import com.epamlearning.models.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class TrainingServiceTest {

    @Mock
    private TrainingDAOImpl trainingDAO;

    @InjectMocks
    private TrainingService trainingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveTraining() {
        Training training = new Training();
        when(trainingDAO.save(training)).thenReturn(Optional.of(training));

        Optional<Training> savedTraining = trainingService.save(training);

        assertTrue(savedTraining.isPresent());
        assertEquals(training, savedTraining.get());
    }

    @Test
    void updateTraining() {
        long trainingId = 1L;
        Training updatedTraining = new Training();
        when(trainingDAO.update(eq(trainingId), any(Training.class))).thenReturn(Optional.of(updatedTraining));

        Optional<Training> training = trainingService.update(trainingId, updatedTraining);

        assertTrue(training.isPresent());
        assertEquals(updatedTraining, training.get());
    }

    @Test
    void findTrainingById() {
        long trainingId = 1L;
        Training training = new Training();
        when(trainingDAO.findById(trainingId)).thenReturn(Optional.of(training));

        Optional<Training> foundTraining = trainingService.findById(trainingId);

        assertTrue(foundTraining.isPresent());
        assertEquals(training, foundTraining.get());
    }

    @Test
    void findAllTrainings() {
        List<Optional<Training>> trainings = new ArrayList<>();
        when(trainingDAO.findAll()).thenReturn(trainings);

        List<Optional<Training>> foundTrainings = trainingService.findAll();

        assertEquals(trainings, foundTrainings);
    }

    @Test
    void deleteTrainingById() {
        long trainingId = 1L;
        when(trainingDAO.findById(trainingId)).thenReturn(Optional.of(new Training()));

        assertDoesNotThrow(() -> trainingService.deleteById(trainingId));
        verify(trainingDAO, times(1)).deleteById(trainingId);
    }

    @Test
    void deleteTrainingByIdNotFound() {
        long trainingId = 1L;
        when(trainingDAO.findById(trainingId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> trainingService.deleteById(trainingId));
        assertEquals("Training with ID " + trainingId + " not found for delete.", exception.getMessage());
        verify(trainingDAO, never()).deleteById(anyLong());
    }

    @Test
    void findByTraineeAndCriteria() {
        Trainee trainee = new Trainee();
        Date dateFrom = new Date();
        Date dateTo = new Date();
        TrainingType trainingType = new TrainingType();
        trainingType.setId(1L);

        List<Optional<Training>> trainings = new ArrayList<>();
        when(trainingDAO.findByTraineeAndCriteria(trainee, dateFrom, dateTo, trainingType)).thenReturn(trainings);

        List<Optional<Training>> foundTrainings = trainingService.findByTraineeAndCriteria(trainee, dateFrom, dateTo, trainingType);

        assertEquals(trainings, foundTrainings);
    }

    @Test
    void findByTrainerAndCriteria() {
        Trainer trainer = new Trainer();
        Date dateFrom = new Date();
        Date dateTo = new Date();
        TrainingType trainingType = new TrainingType();
        trainingType.setId(1L);

        List<Optional<Training>> trainings = new ArrayList<>();
        when(trainingDAO.findByTrainerAndCriteria(trainer, dateFrom, dateTo, trainingType)).thenReturn(trainings);

        List<Optional<Training>> foundTrainings = trainingService.findByTrainerAndCriteria(trainer, dateFrom, dateTo, trainingType);

        assertEquals(trainings, foundTrainings);
    }

//    @Test
//    void updateTraineeTrainers() {
//        // Arrange
//        long traineeId = 1L;
//        Trainee trainee = new Trainee();
//        trainee.setId(traineeId);
//        List<Trainer> trainers = new ArrayList<>();
//        Trainer trainer1 = new Trainer();
//        trainer1.setId(100L); // Set some ID for trainer1
//        Trainer trainer2 = new Trainer();
//        trainer2.setId(101L); // Set some ID for trainer2
//        trainers.add(trainer1);
//        trainers.add(trainer2);
//
//        List<Optional<Training>> traineeTrainings = new ArrayList<>();
//        Training training1 = new Training();
//        training1.setId(200L); // Set some ID for training1
//        training1.setTrainer(trainer1);
//        training1.setTrainee(trainee);
//        Training training2 = new Training();
//        training2.setId(201L); // Set some ID for training2
//        training2.setTrainer(trainer2);
//        training2.setTrainee(trainee);
//        traineeTrainings.add(Optional.of(training1));
//        traineeTrainings.add(Optional.of(training2));
//
//
//        when(trainingDAO.findByTrainee(traineeId)).thenReturn(traineeTrainings);
//        when(trainingDAO.updateTrainingTrainer(anyLong(), any()))
//                .thenReturn(training1) // assuming a Training object is returned by your updateTrainingTrainer method
//                .thenReturn(training2); // assuming a Training object is returned by your updateTrainingTrainer method
//
//        // Act
//        List<Optional<Trainer>> updatedTrainers = trainingService.updateTraineeTrainers(traineeId, trainers);
//
//        // Assert
//        assertEquals(traineeTrainings, updatedTrainers);
//
//        // Verify interactions with the mock
//        verify(trainingDAO, times(traineeTrainings.size())).updateTrainingTrainer(anyLong(), any(Trainer.class));
//        verify(trainingDAO).findTrainersByTrainee(traineeId);
//    }

}
