package com.epamlearning.daos;

import com.epamlearning.exceptions.NotFoundException;
import com.epamlearning.models.*;
import com.epamlearning.storage.InMemoryStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TrainingDAOTest {

    private TrainingDAOImpl trainingDAO;
    private InMemoryStorage inMemoryStorage;

    @BeforeEach
    public void setUp() {
        inMemoryStorage = new InMemoryStorage(); // Assuming you have an InMemoryStorage implementation
        trainingDAO = new TrainingDAOImpl();
        trainingDAO.setInMemoryStorage(inMemoryStorage);
    }

    @Test
    public void testSaveTraining() {
        Training training = createTraining(1L, "Test Training", new Date(), new BigDecimal(20));
        Training savedTraining = trainingDAO.saveTraining(training);

        assertNotNull(savedTraining);
        assertEquals(inMemoryStorage.getStorageTrainings().get(training.getId()), savedTraining);
    }

    @Test
    public void testUpdateExistingTraining(){
        Training training = createTraining(1L, "Test Training", new Date(), new BigDecimal(20));

        inMemoryStorage.getStorageTrainings().put(1L, training);

        Training updatedTraining = trainingDAO.updateTraining(training);

        assertNotNull(updatedTraining);
        assertEquals(inMemoryStorage.getStorageTrainings().get(1L), updatedTraining);
    }

    @Test
    public void testUpdateNonExistingTraining(){
        Training training = createTraining(1L, "Test Training", new Date(), new BigDecimal(20));

        assertThrows(NotFoundException.class, () -> {
            trainingDAO.updateTraining(training);
        });
    }

    @Test
    public void TestFindExistingTrainingById(){
        Training training = createTraining(1L, "Test Training", new Date(), new BigDecimal(20));
        inMemoryStorage.getStorageTrainings().put(1L, training);

        Training foundTraining = trainingDAO.findTrainingById(training.getId());

        assertNotNull(foundTraining);
        assertEquals(training, foundTraining);
    }

    @Test
    public void TestFindNonExistingTrainingById(){
        Training training = createTraining(1L, "Test Training", new Date(), new BigDecimal(20));

        assertThrows(NotFoundException.class, () -> {
            trainingDAO.findTrainingById(training.getId());
        });
    }

    @Test
    public void testDeleteExistingTraining(){
        Training training = createTraining(1L, "Test Training", new Date(), new BigDecimal(20));
        inMemoryStorage.getStorageTrainings().put(1L, training);

        trainingDAO.deleteTrainingById(training.getId());

        assertNull(inMemoryStorage.getStorageTrainees().get(1L));
    }

    @Test
    public void testDeleteNonExistingTraining(){
        Training training = createTraining(1L, "Test Training", new Date(), new BigDecimal(20));

        assertThrows(NotFoundException.class, () -> {
            trainingDAO.deleteTrainingById(training.getId());
        });
    }

    @Test
    public void testFindAllTrainings(){
        Training training1 = createTraining(1L, "Test Training", new Date(), new BigDecimal(20));
        Training training2 = createTraining(2L, "Test Training 2", new Date(), new BigDecimal(10));

        inMemoryStorage.getStorageTrainings().put(1L, training1);
        inMemoryStorage.getStorageTrainings().put(2L, training2);

        List<Training> foundTrainings = trainingDAO.findAllTrainings();

        assertNotNull(foundTrainings);
        assertEquals(2, foundTrainings.size());
        assertTrue(foundTrainings.contains(training1));
        assertTrue(foundTrainings.contains(training2));
    }

    private Training createTraining(Long id, String name, Date date, BigDecimal duration) {
        Training training = new Training();
        training.setId(id);
        training.setTrainingName(name);
        training.setTrainingDate(date);
        training.setTrainingDuration(duration);
        training.setTrainer(createTrainer(1L, "John", "Doe"));
        training.setTrainee(createTrainee(1L, "John", "Doe", new Date(), "Test Address"));
        training.setTrainingType(new TrainingType());
        return training;
    }

    private Trainee createTrainee(Long id, String firstName, String lastName, Date dateOfBirth, String address) {
        Trainee trainee = new Trainee();
        trainee.setId(id);
        trainee.setUser(createUser(id, firstName, lastName));
        trainee.setDateOfBirth(dateOfBirth);
        trainee.setAddress(address);
        return trainee;
    }

    private Trainer createTrainer(Long id, String firstName, String lastName) {
        Trainer trainer = new Trainer();
        trainer.setId(id);
        trainer.setUser(createUser(id, firstName, lastName));
        trainer.setSpecialization(new TrainingType());
        return trainer;
    }

    private User createUser(Long id, String firstName, String lastName) {
        User user = new User();
        user.setId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setActive(true);
        return user;
    }

}
