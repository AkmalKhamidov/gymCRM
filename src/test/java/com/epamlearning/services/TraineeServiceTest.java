package com.epamlearning.services;

import com.epamlearning.daos.TraineeDAO;
import com.epamlearning.exceptions.NotFoundException;
import com.epamlearning.models.Trainee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class TraineeServiceTest {

    @InjectMocks
    private TraineeService traineeService;

    @Mock
    private TraineeDAO traineeDAO;

    private Trainee trainee;

    @BeforeEach
    public void setUp() {
        trainee = new Trainee();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveTrainee() {
        when(traineeDAO.saveTrainee(trainee)).thenReturn(trainee);

        Trainee createdTrainee = traineeService.createTrainee(trainee);

        assertEquals(trainee, createdTrainee);
    }

    @Test
    public void testUpdateExistingTrainee() {
        when(traineeDAO.updateTrainee(trainee)).thenReturn(trainee);

        Trainee updatedTrainee = traineeService.updateTrainee(trainee);

        assertEquals(trainee, updatedTrainee);
    }

    @Test
    public void testUpdateNonExistingTrainee() {

        doThrow(NotFoundException.class).when(traineeDAO).updateTrainee(trainee);

        assertThrows(NotFoundException.class, () -> traineeService.updateTrainee(trainee));
    }

    @Test
    public void testDeleteExistingTrainee(){
        trainee.setId(1L);
        when(traineeDAO.findTraineeById(trainee.getId())).thenReturn(trainee);

        traineeService.deleteTrainee(trainee.getId());
        verify(traineeDAO, times(1)).deleteTraineeById(1L);
    }

    @Test
    public void testDeleteNonExistingTrainee(){
        trainee.setId(1L);

        doThrow(NotFoundException.class).when(traineeDAO).deleteTraineeById(trainee.getId());

        assertThrows(NotFoundException.class, () -> traineeService.deleteTrainee(1L));
    }

    @Test
    public void testFindExistingTraineeById(){
        trainee.setId(1L);
        when(traineeDAO.findTraineeById(trainee.getId())).thenReturn(trainee);

        Trainee foundTrainee = traineeService.getTraineeById(trainee.getId());

        assertEquals(trainee, foundTrainee);
    }

    @Test
    public void testFindNonExistingTraineeById(){
        trainee.setId(1L);

        doThrow(NotFoundException.class).when(traineeDAO).findTraineeById(trainee.getId());

        assertThrows(NotFoundException.class, () -> traineeService.getTraineeById(1L));
    }

    @Test
    public void testFindAllTrainees(){
        trainee.setId(1L);
        when(traineeDAO.findAllTrainees()).thenReturn(java.util.List.of(trainee));

        List<Trainee> foundTrainees = traineeService.getAllTrainees();

        assertEquals(List.of(trainee), foundTrainees);
    }

}
