package com.epamlearning.daos;

import com.epamlearning.exceptions.NotFoundException;
import com.epamlearning.models.Trainee;
import com.epamlearning.models.User;
import com.epamlearning.storage.InMemoryStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TraineeDAOTest {
    private TraineeDAOImpl traineeDAO;
    private InMemoryStorage inMemoryStorage;

    @BeforeEach
    public void setUp() {
        inMemoryStorage = new InMemoryStorage(); // Assuming you have an InMemoryStorage implementation
        traineeDAO = new TraineeDAOImpl();
        traineeDAO.setInMemoryStorage(inMemoryStorage);
    }

    @Test
    public void testSaveTrainee() {
        Trainee trainee = createTrainee(1L, "John", "Doe", new Date(), "Test Address");
        Trainee savedTrainee = traineeDAO.saveTrainee(trainee);

        assertNotNull(savedTrainee);
        assertEquals(inMemoryStorage.getStorageTrainees().get(trainee.getId()), savedTrainee);
    }

    @Test
    public void testUpdateExistingTrainee(){
        Trainee trainee = createTrainee(1L, "John", "Doe", new Date(), "Test Address");

        inMemoryStorage.getStorageTrainees().put(1L, trainee);

        Trainee updatedTrainee = traineeDAO.updateTrainee(trainee);

        assertNotNull(updatedTrainee);
        assertEquals(inMemoryStorage.getStorageTrainees().get(1L), updatedTrainee);
    }

    @Test
    public void testUpdateNonExistingTrainee(){
        Trainee trainee = createTrainee(1L, "John", "Doe", new Date(), "Test Address");

        assertThrows(NotFoundException.class, () -> traineeDAO.updateTrainee(trainee));
    }

    @Test
    public void TestFindExistingTraineeById(){
        Trainee trainee = createTrainee(1L, "John", "Doe", new Date(), "Test Address");
        inMemoryStorage.getStorageTrainees().put(1L, trainee);

        Trainee foundTrainee = traineeDAO.findTraineeById(trainee.getId());

        assertNotNull(foundTrainee);
        assertEquals(trainee, foundTrainee);
    }

    @Test
    public void TestFindNonExistingTraineeById(){
        Trainee trainee = createTrainee(1L, "John", "Doe", new Date(), "Test Address");

        assertThrows(NotFoundException.class, () -> traineeDAO.findTraineeById(trainee.getId()));
    }

    @Test
    public void testDeleteExistingTrainee(){
        Trainee trainee = createTrainee(1L, "John", "Doe", new Date(), "Test Address");
        inMemoryStorage.getStorageTrainees().put(1L, trainee);

        traineeDAO.deleteTraineeById(trainee.getId());

        assertNull(inMemoryStorage.getStorageTrainees().get(1L));
    }

    @Test
    public void testDeleteNonExistingTrainee(){
        Trainee trainee = createTrainee(1L, "John", "Doe", new Date(), "Test Address");

        assertThrows(NotFoundException.class, () -> traineeDAO.deleteTraineeById(trainee.getId()));
    }

    @Test
    public void testFindAllTrainees(){
        Trainee trainee1 = createTrainee(1L, "John", "Doe", new Date(), "Test Address");
        Trainee trainee2 = createTrainee(2L, "Simon", "Kevin", new Date(), "Test Address2");

        inMemoryStorage.getStorageTrainees().put(1L, trainee1);
        inMemoryStorage.getStorageTrainees().put(2L, trainee2);

        List<Trainee> foundTrainees = traineeDAO.findAllTrainees();

        assertNotNull(foundTrainees);
        assertEquals(2, foundTrainees.size());
        assertTrue(foundTrainees.contains(trainee1));
        assertTrue(foundTrainees.contains(trainee2));
    }

    private Trainee createTrainee(Long id, String firstName, String lastName, Date dateOfBirth, String address) {
        Trainee trainee = new Trainee();
        trainee.setId(id);
        trainee.setUser(createUser(id, firstName, lastName));
        trainee.setDateOfBirth(dateOfBirth);
        trainee.setAddress(address);
        return trainee;
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
