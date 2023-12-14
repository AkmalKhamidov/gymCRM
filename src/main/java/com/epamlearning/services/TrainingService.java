package com.epamlearning.services;

import com.epamlearning.daos.TrainingDAOImpl;
import com.epamlearning.exceptions.NotFoundException;
import com.epamlearning.models.Trainee;
import com.epamlearning.models.Trainer;
import com.epamlearning.models.Training;
import com.epamlearning.models.TrainingType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class TrainingService implements EntityService<Training> {

    private TrainingDAOImpl trainingDAO;

    @Autowired
    public TrainingService(TrainingDAOImpl trainingDAO) {
        this.trainingDAO = trainingDAO;
    }

    @Override
    public Optional<Training> save(Training training) {
        return trainingDAO.save(training);
    }

    @Override
    public Optional<Training> update(Long id, Training training) {
        Optional<Training> trainingUpdated = trainingDAO.update(id, training);
        if (trainingUpdated.isEmpty()) {
            log.warn("Training with ID: {} not found for update.", id);
            throw new NotFoundException("Training with ID " + id + " not found for update.");
        }
        return trainingUpdated;
    }

    @Override
    public Optional<Training> findById(Long id) {
        Optional<Training> training = trainingDAO.findById(id);
        if (training.isEmpty()) {
            log.warn("Training with ID: {} not found.", id);
            throw new NotFoundException("Training with ID " + id + " not found.");
        }
        return training;
    }

    @Override
    public List<Optional<Training>> findAll() {
        return trainingDAO.findAll();
    }

    @Override
    public void deleteById(Long id) {
        Optional<Training> training = trainingDAO.findById(id);
        if (training.isEmpty()) {
            log.warn("Training with ID: {} not found for delete.", id);
            throw new NotFoundException("Training with ID " + id + " not found for delete.");
        } else {
            trainingDAO.deleteById(id);
        }
    }

    public List<Optional<Training>> findByTraineeAndCriteria(Trainee trainee, Date dateFrom, Date dateTo, TrainingType trainingType) {
        return trainingDAO.findByTraineeAndCriteria(trainee, dateFrom, dateTo, trainingType);
    }

    public void deleteTrainingsByTrainee(Long traineeId){
        if(trainingDAO.findByTrainee(traineeId).isEmpty()){
            log.warn("Trainings with trainee ID: {} not found for delete.", traineeId);
            throw new NotFoundException("Trainings with trainee ID " + traineeId + " not found for delete.");
        } else {
            trainingDAO.deleteTrainingByTrainee(traineeId);
        }
    }

    @Override
    public Optional<Training> findByUsername(String username) {
        return Optional.empty();
    }

    public List<Optional<Training>> findByTrainerAndCriteria(Trainer trainer, Date dateFrom, Date dateTo, TrainingType trainingType) {
        return trainingDAO.findByTrainerAndCriteria(trainer, dateFrom, dateTo, trainingType);
    }
    // TODO: rewrite by using getter/setter update
    // 1. if the same or not
    // 2. if not we update them
    public List<Optional<Trainer>> updateTraineeTrainers(Long traineeId, List<Trainer> trainers) {
        // Step 1: get Trainee by traineeId
        // Step 2: compare old list of trainers with new list
        List<Optional<Training>> traineeTrainings = trainingDAO.findByTrainee(traineeId);
        if(traineeTrainings.size() == trainers.size()) {
            for(int i = 0; i < traineeTrainings.size(); i++) {
                trainingDAO.updateTrainingTrainer(traineeTrainings.get(i).get().getId(), trainers.get(i));
            }
            log.info("Trainee with ID " + traineeId + " has " + "trainers: " + trainers);
        } else {
            log.warn("Trainee with ID " + traineeId + " has " + traineeTrainings.size() + " trainings, but " + trainers.size() + " trainers were provided.");
            throw new RuntimeException("Trainee with ID " + traineeId + " has " + traineeTrainings.size() + " trainings, but " + trainers.size() + " trainers were provided.");
        }
        return trainingDAO.findTrainersByTrainee(traineeId);
    }


}
