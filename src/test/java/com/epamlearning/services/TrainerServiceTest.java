package com.epamlearning.services;

import com.epamlearning.daos.TrainerDAO;
import com.epamlearning.exceptions.NotFoundException;
import com.epamlearning.models.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class TrainerServiceTest {

    @InjectMocks
    private TrainerService trainerService;

    @Mock
    private TrainerDAO trainerDAO;

    private Trainer trainer;

    @BeforeEach
    public void setUp() {
        trainer = new Trainer();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveTrainer() {
        when(trainerDAO.saveTrainer(trainer)).thenReturn(trainer);

        Trainer createdTrainer = trainerService.createTrainer(trainer);

        assertEquals(trainer, createdTrainer);
    }

    @Test
    public void testUpdateExistingTrainer() {
        when(trainerDAO.updateTrainer(trainer)).thenReturn(trainer);

        Trainer updatedTrainer = trainerService.updateTrainer(trainer);

        assertEquals(trainer, updatedTrainer);
    }

    @Test
    public void testUpdateNonExistingTrainer() {

        doThrow(NotFoundException.class).when(trainerDAO).updateTrainer(trainer);

        assertThrows(NotFoundException.class, () -> trainerService.updateTrainer(trainer));
    }

    @Test
    public void testFindExistingTrainerById(){
        trainer.setId(1L);
        when(trainerDAO.findTrainerById(trainer.getId())).thenReturn(trainer);

        Trainer foundTrainer = trainerService.getTrainerById(trainer.getId());

        assertEquals(trainer, foundTrainer);
    }

    @Test
    public void testFindNonExistingTrainerById(){
        trainer.setId(1L);

        doThrow(NotFoundException.class).when(trainerDAO).findTrainerById(trainer.getId());

        assertThrows(NotFoundException.class, () -> trainerService.getTrainerById(1L));
    }

    @Test
    public void testFindAllTrainers(){
        trainer.setId(1L);
        when(trainerDAO.findAllTrainers()).thenReturn(java.util.List.of(trainer));

        List<Trainer> foundTrainers = trainerService.getAllTrainers();

        assertEquals(List.of(trainer), foundTrainers);
    }

}
