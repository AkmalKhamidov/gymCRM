package com.epamlearning.services;

import com.epamlearning.daos.TrainingDAO;
import com.epamlearning.exceptions.NotFoundException;
import com.epamlearning.models.Training;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class TrainingServiceTest {

    @InjectMocks
    private TrainingService trainingService;

    @Mock
    private TrainingDAO trainingDAO;

    private Training training;

    @BeforeEach
    public void setUp() {
        training = new Training();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveTraining() {
        when(trainingDAO.saveTraining(training)).thenReturn(training);

        Training createdTraining = trainingService.createTraining(training);

        assertEquals(training, createdTraining);
    }

    @Test
    public void testFindExistingTrainingById(){
        training.setId(1L);
        when(trainingDAO.findTrainingById(training.getId())).thenReturn(training);

        Training foundTraining = trainingService.getTrainingById(training.getId());

        assertEquals(training, foundTraining);
    }

    @Test
    public void testFindNonExistingTrainingById(){
        training.setId(1L);

        doThrow(NotFoundException.class).when(trainingDAO).findTrainingById(training.getId());

        assertThrows(NotFoundException.class, () -> trainingService.getTrainingById(1L));
    }

    @Test
    public void testFindAllTrainings(){
        training.setId(1L);
        when(trainingDAO.findAllTrainings()).thenReturn(java.util.List.of(training));

        List<Training> foundTrainings = trainingService.getAllTrainings();

        assertEquals(List.of(training), foundTrainings);
    }

}
