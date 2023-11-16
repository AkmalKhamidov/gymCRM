package com.epamlearning.daos;

import com.epamlearning.exceptions.NotFoundException;
import com.epamlearning.models.Trainee;
import com.epamlearning.storage.InMemoryStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
@Slf4j
public class TraineeDAOImpl implements TraineeDAO {

    private InMemoryStorage inMemoryStorage;

    @Autowired
    public void setInMemoryStorage(InMemoryStorage inMemoryStorage) {
        this.inMemoryStorage = inMemoryStorage;
    }

    @Override
    public Trainee saveTrainee(Trainee trainee) {
        inMemoryStorage.getStorageTrainees().put(trainee.getId(), trainee);
        log.info("Trainee created: {}", trainee);
        return trainee;
    }

    @Override
    public Trainee updateTrainee(Trainee trainee) {
        if (inMemoryStorage.getStorageTrainees().containsKey(trainee.getId())) {
            inMemoryStorage.getStorageTrainees().put(trainee.getId(), trainee);
            log.info("Trainee updated: {}", trainee);
            return trainee;
        } else {
            log.warn("Attempted to update non-existing Trainee with ID {}", trainee.getId());
            throw new NotFoundException("Trainee with ID " + trainee.getId() + " not found for update.");
        }
    }

    @Override
    public Trainee findTraineeById(Long id) {
        Trainee trainee = inMemoryStorage.getStorageTrainees().get(id);
        if (trainee != null) {
            log.info("Trainee found by ID. Trainee: {}", trainee);
            return trainee;
        } else {
            log.warn("Trainee not found by ID. ID: {}", id);
            throw new NotFoundException("Trainee with ID " + id + " not found.");
        }
    }

    @Override
    public List<Trainee> findAllTrainees() {
        Collection<Trainee> traineeValues = inMemoryStorage.getStorageTrainees().values();

        // Convert the values to a new ArrayList
        List<Trainee> trainees = new ArrayList<>(traineeValues);
        log.info("All trainees found: {}", trainees);
        return trainees;
    }

    @Override
    public void deleteTraineeById(Long id) {
        if (inMemoryStorage.getStorageTrainees().containsKey(id)) {
            inMemoryStorage.getStorageTrainees().remove(id);
            log.info("Trainee deleted by ID. ID: {}, Trainee: {}", id, inMemoryStorage.getStorageTrainees().get(id));
        } else {
            log.warn("Attempted to delete non-existing Trainee with ID {}", id);
            throw new NotFoundException("Trainee with ID " + id + " not found for delete.");
        }
    }


}
