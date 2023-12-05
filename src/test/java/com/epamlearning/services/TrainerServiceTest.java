package com.epamlearning.services;

import com.epamlearning.daos.TrainerDAOImpl;
import com.epamlearning.exceptions.NotAuthenticated;
import com.epamlearning.exceptions.NotFoundException;
import com.epamlearning.models.Trainee;
import com.epamlearning.models.Trainer;
import com.epamlearning.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class TrainerServiceTest {

    @Mock
    private TrainerDAOImpl trainerDAO;

    @Mock
    private UserService userService;

    @InjectMocks
    private TrainerService trainerService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setUsername("TestUsername");
        user.setFirstName("TestFirstName");
        user.setLastName("TestLastName");
        user.setActive(true);
    }

    @Test
    void saveTrainer() {
        Trainer trainer = new Trainer();
        when(trainerDAO.save(trainer)).thenReturn(Optional.of(trainer));

        Optional<Trainer> savedTrainer = trainerService.save(trainer);

        assertTrue(savedTrainer.isPresent());
        assertEquals(trainer, savedTrainer.get());
    }

    @Test
    void updateTrainer() {
        long trainerId = 1L;
        Trainer updatedTrainer = new Trainer();
        when(trainerDAO.update(eq(trainerId), any(Trainer.class))).thenReturn(Optional.of(updatedTrainer));

        Optional<Trainer> trainer = trainerService.update(trainerId, updatedTrainer);

        assertTrue(trainer.isPresent());
        assertEquals(updatedTrainer, trainer.get());
    }

    @Test
    void findTrainerById() {
        long trainerId = 1L;
        Trainer trainer = new Trainer();
        when(trainerDAO.findById(trainerId)).thenReturn(Optional.of(trainer));

        Optional<Trainer> foundTrainer = trainerService.findById(trainerId);

        assertTrue(foundTrainer.isPresent());
        assertEquals(trainer, foundTrainer.get());
    }

    @Test
    void findAllTrainers() {
        List<Optional<Trainer>> trainers = new ArrayList<>();
        when(trainerDAO.findAll()).thenReturn(trainers);

        List<Optional<Trainer>> foundTrainers = trainerService.findAll();

        assertEquals(trainers, foundTrainers);
    }

    @Test
    void deleteTrainerById() {
        long trainerId = 1L;
        when(trainerDAO.findById(trainerId)).thenReturn(Optional.of(new Trainer()));

        assertDoesNotThrow(() -> trainerService.deleteById(trainerId));
        verify(trainerDAO, times(1)).deleteById(trainerId);
    }

    @Test
    void deleteTrainerByIdNotFound() {
        long trainerId = 1L;
        when(trainerDAO.findById(trainerId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> trainerService.deleteById(trainerId));
        assertEquals("Trainer with ID " + trainerId + " not found for delete.", exception.getMessage());
        verify(trainerDAO, never()).deleteById(anyLong());
    }

    @Test
    void findByUsername() {
        String username = "testUser";
        Trainer trainer = new Trainer();
        when(trainerDAO.findByUserName(username)).thenReturn(Optional.of(trainer));

        Optional<Trainer> foundTrainer = trainerService.findByUsername(username);

        assertTrue(foundTrainer.isPresent());
        assertEquals(trainer, foundTrainer.get());
    }

    @Test
    void findByUsernameNotFound() {
        String username = "nonExistentUser";
        when(trainerDAO.findByUserName(username)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> trainerService.findByUsername(username));
        assertEquals("Trainer with username " + username + " not found.", exception.getMessage());
    }

    @Test
    void authenticateTrainer() {
        String username = "testUser";
        String password = "testPassword";
        Trainer trainer = new Trainer();
        user.setPassword(password);
        trainer.setUser(user);
        when(trainerDAO.findByUserName(username)).thenReturn(Optional.of(trainer));

        assertTrue(trainerService.authenticate(username, password));
    }

    @Test
    void authenticateTrainerNotFound() {
        String username = "nonExistentUser";
        String password = "testPassword";
        when(trainerDAO.findByUserName(username)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> trainerService.authenticate(username, password));
        assertEquals("Trainer with username " + username + " not found.", exception.getMessage());
    }

    @Test
    void authenticateTrainerWrongPassword() {
        String username = "testUser";
        String correctPassword = "correctPassword";
        String wrongPassword = "wrongPassword";
        Trainer trainer = new Trainer();
        user.setPassword(correctPassword);
        trainer.setUser(user);
        when(trainerDAO.findByUserName(username)).thenReturn(Optional.of(trainer));

        NotAuthenticated exception = assertThrows(NotAuthenticated.class, () -> trainerService.authenticate(username, wrongPassword));
        assertEquals("Wrong password. Username: " + username, exception.getMessage());
    }

    @Test
    void updateTrainerActiveStatus() {
        long trainerId = 1L;
        boolean newActiveStatus = true;
        Trainer updatedTrainer = new Trainer();
        user.setActive(newActiveStatus);
        updatedTrainer.setUser(user);
        when(trainerDAO.findById(trainerId)).thenReturn(Optional.of(updatedTrainer));
        when(userService.updateActive(anyLong(), eq(newActiveStatus))).thenReturn(Optional.of(new User()));

        Optional<Trainer> trainer = trainerService.updateActive(trainerId, newActiveStatus);

        assertTrue(trainer.isPresent());
        assertEquals(updatedTrainer, trainer.get());
        verify(userService, times(1)).updateActive(anyLong(), eq(newActiveStatus));
    }

    @Test
    void updateTrainerPassword() {
        long trainerId = 1L;
        String newPassword = "newPassword";
        Trainer updatedTrainer = new Trainer();
        user.setPassword(newPassword);
        updatedTrainer.setUser(user);
        when(trainerDAO.findById(trainerId)).thenReturn(Optional.of(updatedTrainer));
        when(userService.updatePassword(anyLong(), eq(newPassword))).thenReturn(Optional.of(new User()));

        Optional<Trainer> trainer = trainerService.updatePassword(trainerId, newPassword);

        assertTrue(trainer.isPresent());
        assertEquals(updatedTrainer, trainer.get());
        verify(userService, times(1)).updatePassword(anyLong(), eq(newPassword));
    }

    @Test
    void findNotAssignedActiveTrainers() {
        Trainee trainee = new Trainee();
        List<Optional<Trainer>> notAssignedActiveTrainers = new ArrayList<>();
        when(trainerDAO.findNotAssignedActiveTrainers(trainee)).thenReturn(notAssignedActiveTrainers);

        List<Optional<Trainer>> foundTrainers = trainerService.findNotAssignedActiveTrainers(trainee);

        assertEquals(notAssignedActiveTrainers, foundTrainers);
    }
}
