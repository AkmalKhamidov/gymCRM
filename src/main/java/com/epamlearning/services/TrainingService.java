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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class TrainingService implements EntityService<Training> {

    private final TrainingDAOImpl trainingDAO;
    private final TraineeService traineeService;
    private final TrainerService trainerService;

    @Autowired
    public TrainingService(TrainingDAOImpl trainingDAO, TraineeService traineeService, TrainerService trainerService) {
        this.trainingDAO = trainingDAO;
        this.traineeService = traineeService;
        this.trainerService = trainerService;
    }

    @Override
    public Optional<Training> save(Training training) {
        if (training == null) {
            log.warn("Training is null.");
            throw new NullPointerException("Training is null.");
        }
        if (!traineeService.hasTrainer(training.getTrainee().getId(), training.getTrainer().getId())
                && !existsTraineeAndTrainerInTrainings(training.getTrainee(), training.getTrainer())) {
            List<Trainer> traineeTrainers = new ArrayList<>(traineeService.findTrainersByTraineeId(training.getTrainee().getId()).stream().flatMap(Optional::stream).toList());
            traineeTrainers.add(training.getTrainer());
            training.setTrainee(traineeService.updateTrainersForTrainee(training.getTrainee().getId(), traineeTrainers).get());
        }
        return trainingDAO.saveOrUpdate(training);
    }

    @Override
    public Optional<Training> findById(Long id) {

        if (id == null) {
            log.warn("ID is null.");
            throw new NullPointerException("ID is null.");
        }

        Optional<Training> training = trainingDAO.findById(id);
        if (training.isEmpty()) {
            log.warn("Training with ID: {} not found.", id);
            throw new NotFoundException("Training with ID " + id + " not found.");
        }
        return training;
    }

    @Override
    public Optional<Training> findByUsername(String username) {
        return Optional.empty();
    }

    public boolean existsTraineeAndTrainerInAnotherTraining(Long trainingId, Long traineeId, Long trainerId) {
        Training training = findById(trainingId).get();
        Trainee trainee = traineeService.findById(traineeId).get();
        Trainer trainer = trainerService.findById(trainerId).get();

        return trainingDAO.existsTraineeAndTrainerInAnotherTraining(training, trainee, trainer);
    }

    public boolean existsTraineeAndTrainerInTrainings(Trainee traineeId, Trainer trainerId) {
        Trainee trainee = traineeService.findById(traineeId.getId()).get();
        Trainer trainer = trainerService.findById(trainerId.getId()).get();

        return trainingDAO.existsTraineeAndTrainerInTraining(trainee, trainer);
    }


    @Override
    public Optional<Training> update(Long id, Training training) {

        if (training == null) {
            log.warn("Training is null.");
            throw new NullPointerException("Training is null.");
        }
        if (training.getTrainingDate() == null) {
            log.warn("StartDate is null.");
            throw new NullPointerException("StartDate is null.");
        }
        if (training.getTrainingDuration() == null) {
            log.warn("TrainingDuration is null.");
            throw new NullPointerException("TrainingDuration is null.");
        }

        Training trainingToUpdate = findById(id).get();
        trainingToUpdate.setTrainingType(training.getTrainingType());
        trainingToUpdate.setTrainingDate(training.getTrainingDate());
        trainingToUpdate.setTrainingDuration(training.getTrainingDuration());

        // check if new trainer is not the same as old one
        // check if old trainer is not in another training with trainee in order to delete it from trainee_trainer table
        if (!trainingToUpdate.getTrainer().getId().equals(training.getTrainer().getId())) {
            // get all trainers for trainee
            List<Trainer> traineeTrainers = new ArrayList<>(traineeService.findTrainersByTraineeId(training.getTrainee().getId()).stream().flatMap(Optional::stream).toList());
            // add new trainer to list
            traineeTrainers.add(training.getTrainer());
            if (!existsTraineeAndTrainerInAnotherTraining(id, trainingToUpdate.getTrainee().getId(), trainingToUpdate.getTrainer().getId())) {
                // distinct trainers (in case if new trainer is already in list) and remove old trainer
                traineeTrainers.stream().filter(trainer -> trainer.getId().equals(trainingToUpdate.getTrainer().getId())).findFirst().ifPresent(traineeTrainers::remove);
            }
            // update trainee with new list of trainers and set trainee to training
            trainingToUpdate.setTrainee(traineeService.updateTrainersForTrainee(training.getTrainee().getId(), traineeTrainers.stream().distinct().toList()).get());
        }
        trainingToUpdate.setTrainer(training.getTrainer());
        return trainingDAO.saveOrUpdate(trainingToUpdate);
    }


    @Override
    public List<Optional<Training>> findAll() {
        return trainingDAO.findAll();
    }

    @Override
    public void deleteById(Long id) {
        Training training = findById(id).get();
        if (!existsTraineeAndTrainerInAnotherTraining(id, training.getTrainee().getId(), training.getTrainer().getId())) {
            List<Trainer> traineeTrainers = new ArrayList<>(traineeService.findTrainersByTraineeId(training.getTrainee().getId()).stream().flatMap(Optional::stream).toList());
            traineeTrainers.stream().filter(trainer -> trainer.getId().equals(training.getTrainer().getId())).findFirst().ifPresent(traineeTrainers::remove);
            training.setTrainee(traineeService.updateTrainersForTrainee(training.getTrainee().getId(), traineeTrainers).get());
        }
        trainingDAO.delete(training);
    }

    public List<Optional<Training>> findByTraineeAndCriteria(String username, Date dateFrom, Date
            dateTo, TrainingType trainingType) {
        return trainingDAO.findByTraineeAndCriteria(traineeService.findByUsername(username).get(), dateFrom, dateTo, trainingType);
    }

    public List<Optional<Training>> findByTrainee(Long traineeId) {
        return trainingDAO.findByTrainee(traineeService.findById(traineeId).get());
    }

    public List<Optional<Training>> findByTrainer(Long trainerId) {
        return trainingDAO.findByTrainer(trainerService.findById(trainerId).get());
    }

    public List<Optional<Trainer>> findTrainersByTraineeFromTrainings(Long traineeId) {
        return trainingDAO.findTrainersByTraineeFromTrainings(traineeService.findById(traineeId).get());
    }

    public void deleteTrainingsByTraineeId(Long traineeId) {
        List<Training> trainings = findByTrainee(traineeId).stream().flatMap(Optional::stream).toList();
        trainings.forEach(training -> {
            traineeService.updateTrainersForTrainee(training.getTrainee().getId(), new ArrayList<>());
            trainingDAO.deleteTrainingByTrainee(training.getTrainee());
        });
    }


    public List<Optional<Training>> findByTrainerAndCriteria(String trainerUsername, Date dateFrom, Date
            dateTo, TrainingType trainingType) {
        if (dateFrom == null) {
            log.warn("DateFrom is null.");
            throw new NullPointerException("DateFrom is null.");
        }
        if (dateTo == null) {
            log.warn("DateTo is null.");
            throw new NullPointerException("DateTo is null.");
        }
        if (trainingType == null) {
            log.warn("TrainingType is null.");
            throw new NullPointerException("TrainingType is null.");
        }

        Optional<Trainer> trainer = trainerService.findByUsername(trainerUsername);
        return trainingDAO.findByTrainerAndCriteria(trainer.get(), dateFrom, dateTo, trainingType);
    }

    public Training createTraining(String trainingName, Date trainingDate, BigDecimal trainingDuration, Trainer
            trainer, Trainee trainee, TrainingType trainingType) {
        if (trainingName == null || trainingName.isEmpty()) {
            log.warn("TrainingName is null.");
            throw new NullPointerException("TrainingName is null.");
        }
        if (trainingDate == null) {
            log.warn("TrainingDate is null.");
            throw new NullPointerException("TrainingDate is null.");
        }
        if (trainingDuration == null) {
            log.warn("TrainingDuration is null.");
            throw new NullPointerException("TrainingDuration is null.");
        }

        Training training = new Training();
        training.setTrainingName(trainingName);
        training.setTrainingDate(trainingDate);
        training.setTrainingDuration(trainingDuration);
        training.setTrainer(trainer);
        training.setTrainee(trainee);
        training.setTrainingType(trainingType);
        return training;
    }

}
