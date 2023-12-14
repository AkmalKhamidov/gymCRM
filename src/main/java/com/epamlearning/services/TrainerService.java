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

// TODO: change setter injection to constructor injection for whole projects

@Service
@Slf4j
public class TrainerService implements EntityService<Trainer> {

    private TrainerDAOImpl trainerDAO;
    private UserService userService;

    @Autowired
    public TrainerService(TrainerDAOImpl trainerDAO, UserService userService) {
        this.trainerDAO = trainerDAO;
        this.userService = userService;
    }

    @Override
    public Optional<Trainer> save(Trainer trainer) {
        return trainerDAO.save(trainer);
    }

    @Override
    public Optional<Trainer> update(Long id, Trainer trainer) {
        Optional<Trainer> trainerUpdated = trainerDAO.update(id, trainer);
        if (trainerUpdated.isEmpty()) {
            log.warn("Trainer with ID: {} not found for update.", id);
            throw new NotFoundException("Trainer with ID " + id + " not found for update.");
        }
        return trainerUpdated;
    }

    @Override
    public Optional<Trainer> findById(Long id) {
        Optional<Trainer> trainer = trainerDAO.findById(id);
        if(trainer.isEmpty()) {
            log.warn("Trainer with ID: {} not found.", id);
            throw new NotFoundException("Trainer with ID " + id + " not found.");
        }
        return trainer;
    }

    @Override
    public List<Optional<Trainer>> findAll() {
        return trainerDAO.findAll();
    }

    @Override
    public void deleteById(Long id) {
        Optional<Trainer> trainer = trainerDAO.findById(id);
        if(trainer.isEmpty()){
            log.warn("Trainer with ID: {} not found for delete.", id);
            throw new NotFoundException("Trainer with ID " + id + " not found for delete.");
        } else {
            trainerDAO.deleteById(id);
        }
    }

    @Override
    public Optional<Trainer> findByUsername(String username) {
        Optional<Trainer> trainer = trainerDAO.findByUserName(username);
        if(trainer.isEmpty()){
            log.warn("Trainer with username: {} not found.", username);
            throw new NotFoundException("Trainer with username " + username + " not found.");
        }
        return trainer;
    }

    public boolean authenticate(String username, String password) {
        Optional<Trainer> trainer = trainerDAO.findByUserName(username);
        if(trainer.isEmpty()){
            log.warn("Trainer with username: {} not found.", username);
            throw new NotFoundException("Trainer with username " + username + " not found.");
        }
        if(trainer.get().getUser().getPassword().equals(password)){
            return true;
        } else {
            log.warn("Wrong password. Username: {} ",username);
            throw new NotAuthenticated("Wrong password. Username: " + username);
        }
    }

    public Optional<Trainer> updateActive(Long id, boolean active) {
        Optional<Trainer> trainerUpdated = trainerDAO.findById(id);
        if (trainerUpdated.isEmpty()) {
            log.warn("Trainee with ID: {} not found for active update.", id);
            throw new NotFoundException("Trainee with ID " + id + " not found for update.");
        }
        trainerUpdated.get().setUser(userService.updateActive(trainerUpdated.get().getUser().getId(), active).get());
        return trainerUpdated;
    }

    public Optional<Trainer> updatePassword(Long id, String password) {
        Optional<Trainer> trainerUpdated = trainerDAO.findById(id);
        if (trainerUpdated.isEmpty()) {
            log.warn("Trainee with ID: {} not found for password update.", id);
            throw new NotFoundException("Trainee with ID " + id + " not found for update.");
        }
        trainerUpdated.get().setUser(userService.updatePassword(trainerUpdated.get().getUser().getId(), password).get());
        log.info("Trainer updated: {}", trainerUpdated);
        return trainerUpdated;
    }

    public List<Optional<Trainer>> findNotAssignedActiveTrainers(Trainee trainee) {
        return trainerDAO.findNotAssignedActiveTrainers(trainee);
    }

    public Trainer createTrainer(User user, TrainingType trainingType) {

        if(user == null) {
            log.warn("User is null.");
            throw new NullPointerException("User is null.");
        }
        if(trainingType == null) {
            log.warn("TrainingType is null.");
            throw new NullPointerException("TrainingType is null.");
        }

        Trainer trainer = new Trainer();
        trainer.setUser(user);
        trainer.setSpecialization(trainingType);
        return trainer;
    }

}
