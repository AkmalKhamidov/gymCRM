package com.epamlearning.services;

import com.epamlearning.daos.TraineeDAOImpl;
import com.epamlearning.exceptions.NotAuthenticated;
import com.epamlearning.exceptions.NotFoundException;
import com.epamlearning.models.Trainee;
import com.epamlearning.models.Trainer;
import com.epamlearning.models.User;
import com.epamlearning.services.UserService;
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

class TraineeServiceTest {

    @Mock
    private TraineeDAOImpl traineeDAO;

    @Mock
    private UserService userService;

    @InjectMocks
    private TraineeService traineeService;

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
    void saveTrainee() {
        Trainee trainee = new Trainee();
        when(traineeDAO.save(trainee)).thenReturn(Optional.of(trainee));

        Optional<Trainee> savedTrainee = traineeService.save(trainee);

        assertTrue(savedTrainee.isPresent());
        assertEquals(trainee, savedTrainee.get());
    }

    @Test
    void updateTrainee() {
        long traineeId = 1L;
        Trainee updatedTrainee = new Trainee();
        when(traineeDAO.update(eq(traineeId), any(Trainee.class))).thenReturn(Optional.of(updatedTrainee));

        Optional<Trainee> trainee = traineeService.update(traineeId, updatedTrainee);

        assertTrue(trainee.isPresent());
        assertEquals(updatedTrainee, trainee.get());
    }

    @Test
    void findTraineeById() {
        long traineeId = 1L;
        Trainee trainee = new Trainee();
        when(traineeDAO.findById(traineeId)).thenReturn(Optional.of(trainee));

        Optional<Trainee> foundTrainee = traineeService.findById(traineeId);

        assertTrue(foundTrainee.isPresent());
        assertEquals(trainee, foundTrainee.get());
    }

    @Test
    void findAllTrainees() {
        List<Optional<Trainee>> trainees = new ArrayList<>();
        when(traineeDAO.findAll()).thenReturn(trainees);

        List<Optional<Trainee>> foundTrainees = traineeService.findAll();

        assertEquals(trainees, foundTrainees);
    }

    @Test
    void deleteTraineeById() {
        long traineeId = 1L;
        when(traineeDAO.findById(traineeId)).thenReturn(Optional.of(new Trainee()));

        assertDoesNotThrow(() -> traineeService.deleteById(traineeId));
        verify(traineeDAO, times(1)).deleteById(traineeId);
    }

    @Test
    void deleteTraineeByIdNotFound() {
        long traineeId = 1L;
        when(traineeDAO.findById(traineeId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> traineeService.deleteById(traineeId));
        assertEquals("Trainee with ID " + traineeId + " not found for delete.", exception.getMessage());
        verify(traineeDAO, never()).deleteById(anyLong());
    }

    @Test
    void findByUsername() {
        String username = "testUser";
        Trainee trainee = new Trainee();
        when(traineeDAO.findByUserName(username)).thenReturn(Optional.of(trainee));

        Optional<Trainee> foundTrainee = traineeService.findByUsername(username);

        assertTrue(foundTrainee.isPresent());
        assertEquals(trainee, foundTrainee.get());
    }

    @Test
    void findByUsernameNotFound() {
        String username = "nonExistentUser";
        when(traineeDAO.findByUserName(username)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> traineeService.findByUsername(username));
        assertEquals("Trainee with username " + username + " not found.", exception.getMessage());
    }

    @Test
    void authenticateTrainee() {
        String username = "testUser";
        String password = "testPassword";
        Trainee trainee = new Trainee();
        User user = new User();
        user.setPassword(password);
        trainee.setUser(user);
        when(traineeDAO.findByUserName(username)).thenReturn(Optional.of(trainee));

        assertTrue(traineeService.authenticate(username, password));
    }

    @Test
    void authenticateTraineeNotFound() {
        String username = "nonExistentUser";
        String password = "testPassword";
        when(traineeDAO.findByUserName(username)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> traineeService.authenticate(username, password));
        assertEquals("Trainee with username " + username + " not found.", exception.getMessage());
    }

    @Test
    void authenticateTraineeWrongPassword() {
        String username = "testUser";
        String correctPassword = "correctPassword";
        String wrongPassword = "wrongPassword";
        Trainee trainee = new Trainee();
        user.setPassword(correctPassword);
        trainee.setUser(user);
        when(traineeDAO.findByUserName(username)).thenReturn(Optional.of(trainee));

        NotAuthenticated exception = assertThrows(NotAuthenticated.class, () -> traineeService.authenticate(username, wrongPassword));
        assertEquals("Wrong password. Username: " + username, exception.getMessage());
    }

    @Test
    void updateTraineeActiveStatus() {
        long traineeId = 1L;
        boolean newActiveStatus = true;
        Trainee updatedTrainee = new Trainee();
        updatedTrainee.setUser(user);
        when(traineeDAO.findById(traineeId)).thenReturn(Optional.of(updatedTrainee));
        when(userService.updateActive(anyLong(), eq(newActiveStatus))).thenReturn(Optional.of(new User()));

        Optional<Trainee> trainee = traineeService.updateActive(traineeId, newActiveStatus);

        assertTrue(trainee.isPresent());
        assertEquals(updatedTrainee, trainee.get());
        verify(userService, times(1)).updateActive(anyLong(), eq(newActiveStatus));
    }

    @Test
    void updateTraineePassword() {
        long traineeId = 1L;
        String newPassword = "newPassword";
        Trainee updatedTrainee = new Trainee();
        updatedTrainee.setUser(user);
        when(traineeDAO.findById(traineeId)).thenReturn(Optional.of(updatedTrainee));
        when(userService.updatePassword(anyLong(), eq(newPassword))).thenReturn(Optional.of(new User()));

        Optional<Trainee> trainee = traineeService.updatePassword(traineeId, newPassword);

        assertTrue(trainee.isPresent());
        assertEquals(updatedTrainee, trainee.get());
        verify(userService, times(1)).updatePassword(anyLong(), eq(newPassword));
    }

    @Test
    void updateTrainersList() {
        long traineeId = 1L;
        List<Trainer> trainers = new ArrayList<>();
        Trainee trainee = new Trainee();
        when(traineeDAO.findById(traineeId)).thenReturn(Optional.of(trainee));
        when(traineeDAO.save(trainee)).thenReturn(Optional.of(trainee));

        Optional<Trainee> updatedTrainee = traineeService.updateTrainersList(traineeId, trainers);

        assertTrue(updatedTrainee.isPresent());
        assertEquals(trainee, updatedTrainee.get());
    }
}
