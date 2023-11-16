package com.epamlearning.daos;

import com.epamlearning.exceptions.NotFoundException;
import com.epamlearning.models.Trainee;
import com.epamlearning.models.Trainer;
import com.epamlearning.storage.InMemoryStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
@Slf4j
public class TrainerDAOImpl implements TrainerDAO {

    private InMemoryStorage inMemoryStorage;

    @Autowired
    public void setInMemoryStorage(InMemoryStorage inMemoryStorage) {
        this.inMemoryStorage = inMemoryStorage;
    }

    @Override
    public Trainer saveTrainer(Trainer trainer) {
        inMemoryStorage.getStorageTrainers().put(trainer.getId(), trainer);
        log.info("Trainer created: {}", trainer);
        return trainer;
    }

    @Override
    public Trainer updateTrainer(Trainer trainer) {
        if (inMemoryStorage.getStorageTrainers().containsKey(trainer.getId())) {
            inMemoryStorage.getStorageTrainers().put(trainer.getId(), trainer);
            log.info("Trainer updated: {}", trainer);
            return trainer;
        } else {
            log.warn("Attempted to update non-existing Trainer with ID {}", trainer.getId());
            throw new NotFoundException("Trainer with ID " + trainer.getId() + " not found for update.");
        }
    }


    @Override
    public Trainer findTrainerById(Long id) {
        Trainer trainer = inMemoryStorage.getStorageTrainers().get(id);
        if (trainer != null) {
            log.info("Trainer found by ID. Trainer: {}", trainer);
            return trainer;
        } else {
            log.warn("Trainer not found by ID. ID: {}", id);
            throw new NotFoundException("Trainer with ID " + id + " not found.");
        }
    }

    @Override
    public List<Trainer> findAllTrainers() {
        Collection<Trainer> trainerValues = inMemoryStorage.getStorageTrainers().values();

        // Convert the values to a new ArrayList
        List<Trainer> trainers = new ArrayList<>(trainerValues);
        log.info("All trainers found: {}", trainers);
        return trainers;
    }


}
