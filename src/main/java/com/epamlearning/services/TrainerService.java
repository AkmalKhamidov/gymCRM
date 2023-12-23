package com.epamlearning.services;

import com.epamlearning.daos.TrainerDAOImpl;
import com.epamlearning.exceptions.NotAuthenticated;
import com.epamlearning.exceptions.NotFoundException;
import com.epamlearning.models.Trainee;
import com.epamlearning.models.Trainer;
import com.epamlearning.models.TrainingType;
import com.epamlearning.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TrainerService implements BaseService<Trainer> {

    private final TrainerDAOImpl trainerDAO;
    private final TraineeService traineeService;
    private final UserService userService;

    @Autowired
    public TrainerService(TrainerDAOImpl trainerDAO, TraineeService traineeService, UserService userService) {
        this.trainerDAO = trainerDAO;
        this.traineeService = traineeService;
        this.userService = userService;
    }

    @Override
    public Optional<Trainer> findById(Long id) {
        if (id == null) {
            log.warn("ID is null.");
            throw new NullPointerException("ID is null.");
        }

        Optional<Trainer> trainer = trainerDAO.findById(id);
        if (trainer.isEmpty()) {
            log.warn("Trainer with ID: {} not found.", id);
            throw new NotFoundException("Trainer with ID " + id + " not found.");
        }
        return trainer;
    }

    @Override
    public Optional<Trainer> findByUsername(String username) {
        if (username == null || username.isEmpty()) {
            log.warn("Username is null.");
            throw new NullPointerException("Username is null.");
        }

        Optional<Trainer> trainer = trainerDAO.findByUserName(username);
        if (trainer.isEmpty()) {
            log.warn("Trainer with username: {} not found.", username);
            throw new NotFoundException("Trainer with username " + username + " not found.");
        }
        return trainer;
    }

    @Override
    public Optional<Trainer> save(Trainer trainer) {
        return trainerDAO.saveOrUpdate(trainer);
    }

    @Override
    public Optional<Trainer> update(Long id, Trainer trainer) {

        if (trainer == null) {
            log.warn("Trainer is null.");
            throw new NullPointerException("Trainer is null.");
        }
        if (trainer.getSpecialization() == null) {
            log.warn("TrainingType is null.");
            throw new NullPointerException("TrainingType is null.");
        }
        userService.userNullVerification(trainer.getUser());

        Trainer trainerToUpdate = findById(id).get();
        trainerToUpdate.setUser(trainer.getUser());
        trainerToUpdate.setSpecialization(trainer.getSpecialization());
        return trainerDAO.saveOrUpdate(trainerToUpdate);
    }


    @Override
    public List<Optional<Trainer>> findAll() {
        return trainerDAO.findAll();
    }

    @Override
    public void deleteById(Long id) {
        trainerDAO.delete(findById(id).get());
    }

    public Long authenticate(String username, String password) {

        if (password == null || password.isEmpty()) {
            log.warn("Password is null.");
            throw new NullPointerException("Password is null.");
        }
        Trainer trainer = findByUsername(username).get();
        if (trainer.getUser().getPassword().equals(password)) {
            return trainer.getId();
        } else {
            log.warn("Wrong password. Username: {} ", username);
            throw new NotAuthenticated("Wrong password. Username: " + username);
        }
    }

    public Optional<Trainer> updateActive(Long id, boolean active) {
        Trainer trainerUpdated = findById(id).get();
        trainerUpdated.getUser().setActive(active);
        return trainerDAO.saveOrUpdate(trainerUpdated);
    }

    public Optional<Trainer> updatePassword(Long id, String password) {
        if (password == null || password.isEmpty()) {
            log.warn("Password is null.");
            throw new NullPointerException("Password is null.");
        }

        Trainer trainerUpdated = findById(id).get();

        trainerUpdated.getUser().setPassword(password);
        return trainerDAO.saveOrUpdate(trainerUpdated);
    }

    public List<Optional<Trainer>> findNotAssignedActiveTrainers(Long traineeId) {
        return trainerDAO.findNotAssignedActiveTrainers(traineeService.findById(traineeId).get());
    }

    public Trainer createTrainer(User user, TrainingType trainingType) {

        if (user == null) {
            log.warn("User is null.");
            throw new NullPointerException("User is null.");
        }

        Trainer trainer = new Trainer();
        trainer.setUser(user);
        trainer.setSpecialization(trainingType);
        return trainer;
    }

    public List<Optional<Trainee>> findTraineesByTrainerId(Long id) {
        return trainerDAO.findTraineesByTrainer(findById(id).get());
    }
}
