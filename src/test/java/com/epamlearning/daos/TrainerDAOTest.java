package com.epamlearning.daos;

import com.epamlearning.exceptions.NotFoundException;
import com.epamlearning.models.Trainer;
import com.epamlearning.models.TrainingType;
import com.epamlearning.models.User;
import com.epamlearning.storage.InMemoryStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TrainerDAOTest {
    private TrainerDAOImpl trainerDAO;
    private InMemoryStorage inMemoryStorage;

    @BeforeEach
    public void setUp() {
        inMemoryStorage = new InMemoryStorage(); // Assuming you have an InMemoryStorage implementation
        trainerDAO = new TrainerDAOImpl();
        trainerDAO.setInMemoryStorage(inMemoryStorage);
    }

    @Test
    public void testSaveTrainer() {
        Trainer trainer = createTrainer(1L, "John", "Doe");
        Trainer savedTrainer = trainerDAO.saveTrainer(trainer);

        assertNotNull(savedTrainer);
        assertEquals(inMemoryStorage.getStorageTrainers().get(trainer.getId()), savedTrainer);
    }

    @Test
    public void testUpdateExistingTrainer(){
        Trainer trainer = createTrainer(1L, "John", "Doe");

        inMemoryStorage.getStorageTrainers().put(1L, trainer);

        Trainer updatedTrainer = trainerDAO.updateTrainer(trainer);

        assertNotNull(updatedTrainer);
        assertEquals(inMemoryStorage.getStorageTrainers().get(1L), updatedTrainer);
    }

    @Test
    public void testUpdateNonExistingTrainer(){
        Trainer trainer = createTrainer(1L, "John", "Doe");

        assertThrows(NotFoundException.class, () -> {
            trainerDAO.updateTrainer(trainer);
        });
    }

    @Test
    public void TestFindExistingTrainerById(){
        Trainer trainer = createTrainer(1L, "John", "Doe");
        inMemoryStorage.getStorageTrainers().put(1L, trainer);

        Trainer foundTrainer = trainerDAO.findTrainerById(trainer.getId());

        assertNotNull(foundTrainer);
        assertEquals(trainer, foundTrainer);
    }

    @Test
    public void TestFindNonExistingTrainerById(){
        Trainer trainer = createTrainer(1L, "John", "Doe");

        assertThrows(NotFoundException.class, () -> {
            trainerDAO.findTrainerById(trainer.getId());
        });
    }

    @Test
    public void testFindAllTrainers(){
        Trainer trainer1 = createTrainer(1L, "John", "Doe");
        Trainer trainer2 = createTrainer(2L, "Simon", "Kevin");

        inMemoryStorage.getStorageTrainers().put(1L, trainer1);
        inMemoryStorage.getStorageTrainers().put(2L, trainer2);

        List<Trainer> foundTrainers = trainerDAO.findAllTrainers();

        assertNotNull(foundTrainers);
        assertEquals(2, foundTrainers.size());
        assertTrue(foundTrainers.contains(trainer1));
        assertTrue(foundTrainers.contains(trainer2));
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
