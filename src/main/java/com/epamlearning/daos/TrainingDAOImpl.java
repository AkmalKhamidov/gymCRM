package com.epamlearning.daos;

import com.epamlearning.exceptions.NotFoundException;
import com.epamlearning.models.Trainer;
import com.epamlearning.models.Training;
import com.epamlearning.storage.InMemoryStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
@Slf4j
public class TrainingDAOImpl implements TrainingDAO {

    private InMemoryStorage inMemoryStorage;

    @Autowired
    public void setInMemoryStorage(InMemoryStorage inMemoryStorage) {
        this.inMemoryStorage = inMemoryStorage;
    }
    @Override
    public Training saveTraining(Training training) {
        inMemoryStorage.getStorageTrainings().put(training.getId(), training);
        log.info("Training created: {}", training);
        return training;
    }

    @Override
    public Training updateTraining(Training training) {
        if (inMemoryStorage.getStorageTrainings().containsKey(training.getId())) {
            inMemoryStorage.getStorageTrainings().put(training.getId(), training);
            log.info("Training updated: {}", training);
            return training;
        } else {
            log.warn("Attempted to update non-existing Training with ID {}", training.getId());
            throw new NotFoundException("Training with ID " + training.getId() + " not found for update.");
        }
    }

    @Override
    public Training findTrainingById(Long id) {
        Training training = inMemoryStorage.getStorageTrainings().get(id);
        if (training != null) {
            log.info("Training found by ID. Trainer: {}", training);
            return training;
        } else {
            log.warn("Training not found by ID. ID: {}", id);
            throw new NotFoundException("Training with ID " + id + " not found.");
        }
    }

    @Override
    public List<Training> findAllTrainings() {
        Collection<Training> trainingValues = inMemoryStorage.getStorageTrainings().values();

        // Convert the values to a new ArrayList
        List<Training> training = new ArrayList<>(trainingValues);
        log.info("All trainers found: {}", training);
        return training;
    }

    @Override
    public void deleteTrainingById(Long id) {
        if (inMemoryStorage.getStorageTrainings().containsKey(id)) {
            inMemoryStorage.getStorageTrainings().remove(id);
            log.info("Training with ID {} deleted", id);
        } else {
            log.warn("Attempted to delete non-existing Training with ID {}", id);
            throw new NotFoundException("Training with ID " + id + " not found for deletion.");
        }
    }
}
