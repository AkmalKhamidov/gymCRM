package com.epamlearning.services;

import com.epamlearning.daos.TrainingTypeDAOImpl;
import com.epamlearning.exceptions.NotFoundException;
import com.epamlearning.models.TrainingType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TrainingTypeService implements EntityService<TrainingType>{

    private final TrainingTypeDAOImpl trainingTypeDAO;

    @Autowired
    public TrainingTypeService(TrainingTypeDAOImpl trainingTypeDAO) {
        this.trainingTypeDAO = trainingTypeDAO;
    }

    @Override
    public Optional<TrainingType> save(TrainingType trainingType) {
        return Optional.empty();
    }

    @Override
    public Optional<TrainingType> update(Long id, TrainingType trainingType) {
        return Optional.empty();
    }

    @Override
    public Optional<TrainingType> findById(Long id) {
        if(id == null) {
            log.warn("ID is null.");
            throw new NullPointerException("ID is null.");
        }
        Optional<TrainingType> trainingType = trainingTypeDAO.findById(id);
        if (trainingType.isEmpty()) {
            log.warn("TrainingType with ID: {} not found.", id);
            throw new NotFoundException("TrainingType with ID " + id + " not found.");
        }
        return trainingType;
    }

    @Override
    public List<Optional<TrainingType>> findAll() {
        return trainingTypeDAO.findAll();
    }

    @Override
    public void deleteById(Long id) {
    }

    @Override
    public Optional<TrainingType> findByUsername(String username) {
        return Optional.empty();
    }
}
