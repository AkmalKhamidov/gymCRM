package com.epamlearning.services;

import com.epamlearning.exceptions.NotAuthenticated;
import com.epamlearning.exceptions.NotFoundException;
import com.epamlearning.models.Trainee;
import com.epamlearning.models.Trainer;
import com.epamlearning.models.User;
import com.epamlearning.repositories.TraineeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class TraineeService implements BaseService<Trainee> {

    private final TraineeRepository traineeRepository;
    private final UserService userService;

    @Autowired
    private TrainingService trainingService;

    @Autowired
    public TraineeService(TraineeRepository traineeRepository, UserService userService) {
        this.traineeRepository = traineeRepository;
        this.userService = userService;
    }

    @Override
    public Optional<Trainee> findById(Long id) {

        if (id == null) {
            log.warn("ID is null.");
            throw new NullPointerException("ID is null.");
        }

        Optional<Trainee> trainee = traineeRepository.findById(id);
        if (trainee.isEmpty()) {
            log.warn("Trainee with ID: {} not found.", id);
            throw new NotFoundException("Trainee with ID " + id + " not found.");
        }
        return trainee;
    }

    @Override
    public Optional<Trainee> findByUsername(String username) {

        if (username == null || username.isEmpty()) {
            log.warn("Username is null.");
            throw new NullPointerException("Username is null.");
        }

        Optional<Trainee> trainee = traineeRepository.findByUsername(username);
        if (trainee.isEmpty()) {
            log.warn("Trainee with username: {} not found.", username);
            throw new NotFoundException("Trainee with username " + username + " not found.");
        }
        return trainee;
    }

    @Override
    public Optional<Trainee> save(Trainee trainee) {
        return Optional.of(traineeRepository.save(trainee));
    }

    @Override
    public Optional<Trainee> update(Long id, Trainee trainee) {
        if (trainee == null) {
            log.warn("Trainee is null.");
            throw new NullPointerException("Trainee is null.");
        }
        userService.userNullVerification(trainee.getUser());

        Trainee traineeToUpdate = findById(id).get();
        traineeToUpdate.setDateOfBirth(trainee.getDateOfBirth());
        traineeToUpdate.setAddress(trainee.getAddress());
        traineeToUpdate.setUser(trainee.getUser());
        return Optional.of(traineeRepository.save(traineeToUpdate));
    }

    @Override
    public List<Optional<Trainee>> findAll() {
        return traineeRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        Trainee trainee = findById(id).get();
        trainingService.deleteTrainingsByTraineeId(trainee.getId());
        traineeRepository.delete(trainee);
    }

    public void deleteByUsername(String username) {
        Trainee trainee = findByUsername(username).get();
        trainingService.deleteTrainingsByTraineeId(trainee.getId());
        traineeRepository.delete(trainee);
    }

    public Long authenticate(String username, String password) {

        if (password == null || password.isEmpty()) {
            log.warn("Password is null.");
            throw new NullPointerException("Password is null.");
        }

        Trainee trainee = findByUsername(username).get();
        if (trainee.getUser().getPassword().equals(password)) {
            return trainee.getId();
        } else {
            log.warn("Wrong password. Username: {} ", username);
            throw new NotAuthenticated("Wrong password. Username: " + username);
        }
    }

    public Optional<Trainee> updateActive(Long id, boolean active) {
        Trainee traineeUpdated = findById(id).get();

        // TODO: ASK MENTORS: which is better approach?
        // 1. traineeUpdated.getUser().setActive(active);
        // 2. traineeUpdated.setUser(userService.updateActive(traineeUpdated.get().getUser().getId(), active).get());

        traineeUpdated.getUser().setActive(active);
        return Optional.of(traineeRepository.save(traineeUpdated));
    }

    public Optional<Trainee> updatePassword(Long id, String password) {

        if (password == null || password.isEmpty()) {
            log.warn("Password is null.");
            throw new NullPointerException("Password is null.");
        }
        Trainee traineeUpdated = findById(id).get();

        // TODO: ASK MENTORS: which is better approach?
        // 1. traineeUpdated.getUser().setPassword(password);
        // 2. traineeUpdated.setUser(userService.updatePassword(traineeUpdated.getUser().getId(), password).get());

        traineeUpdated.getUser().setPassword(password);
        return Optional.of(traineeRepository.save(traineeUpdated));
    }

    public Optional<Trainee> updateTrainersForTrainee(Long traineeId, List<Trainer> trainers) {

        Trainee traineeToUpdate = findById(traineeId).get();

        traineeToUpdate.setTrainers(trainers);

        // Save the updated trainee
        return Optional.of(traineeRepository.save(traineeToUpdate));
    }

    public List<Optional<Trainer>> findTrainersByTraineeId(Long id) {
        return traineeRepository.findTrainersByTrainee(findById(id).get());
    }

    public boolean hasTrainer(Long traineeId, Long trainerId) {
        List<Optional<Trainer>> traineeTrainer = findTrainersByTraineeId(traineeId);
        if(traineeTrainer.stream().filter(trainer -> trainer.get().getId().equals(trainerId)).toList().isEmpty()) {
            log.info("Trainee with ID: {} has no trainer with ID: {}.", traineeId, trainerId);
            return false;
        } else {
            log.info("Trainee with ID: {} has no trainer with ID: {}.", traineeId, trainerId);
            return true;
        }
    }

    public Trainee createTrainee(User user, String address, Date dateOfBirth) {
        if (user == null) {
            log.warn("User is null.");
            throw new NullPointerException("User is null.");
        }
        if (address == null || address.isEmpty()) {
            log.warn("Address is null.");
            throw new NullPointerException("Address is null.");
        }
        if (dateOfBirth == null) {
            log.warn("Date of birth is null.");
            throw new NullPointerException("Date of birth is null.");
        }

        Trainee trainee = new Trainee();
        trainee.setAddress(address);
        trainee.setDateOfBirth(dateOfBirth);
        trainee.setUser(user);
        return trainee;
    }

}
