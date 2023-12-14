package com.epamlearning.services;

import com.epamlearning.daos.TraineeDAOImpl;
import com.epamlearning.daos.TrainingDAOImpl;
import com.epamlearning.exceptions.NotAuthenticated;
import com.epamlearning.exceptions.NotFoundException;
import com.epamlearning.models.Trainee;
import com.epamlearning.models.Trainer;
import com.epamlearning.models.Training;
import com.epamlearning.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class TraineeService implements EntityService<Trainee> {

    private TraineeDAOImpl traineeDAO;

    private TrainingDAOImpl trainingDAO;
    private UserService userService;

    @Autowired
    public TraineeService(TraineeDAOImpl traineeDAO, TrainingDAOImpl trainingDAO, UserService userService) {
        this.traineeDAO = traineeDAO;
        this.trainingDAO = trainingDAO;
        this.userService = userService;
    }

    @Override
    public Optional<Trainee> save(Trainee trainee) {
        return traineeDAO.save(trainee);
    }

    @Override
    public Optional<Trainee> update(Long id, Trainee trainee) {
        Optional<Trainee> traineeUpdated = traineeDAO.update(id, trainee);
        if (traineeUpdated.isEmpty()) {
            log.warn("Trainee with ID: {} not found for update.", id);
            throw new NotFoundException("Trainee with ID " + id + " not found for update.");
        }
        return traineeUpdated;
    }

    @Override
    public Optional<Trainee> findById(Long id) {
        Optional<Trainee> trainee = traineeDAO.findById(id);
        if (trainee.isEmpty()) {
            log.warn("Trainee with ID: {} not found.", id);
            throw new NotFoundException("Trainee with ID " + id + " not found.");
        }
        return trainee;
    }

    @Override
    public List<Optional<Trainee>> findAll() {
        return traineeDAO.findAll();
    }

    @Override
    public void deleteById(Long id) {
        Optional<Trainee> trainee = traineeDAO.findById(id);
        if (trainee.isEmpty()) {
            log.warn("Trainee with ID: {} not found for delete.", id);
            throw new NotFoundException("Trainee with ID " + id + " not found for delete.");
        } else
            traineeDAO.deleteById(id);
    }

    @Override
    public Optional<Trainee> findByUsername(String username) {
        Optional<Trainee> trainee = traineeDAO.findByUserName(username);
        if (trainee.isEmpty()) {
            log.warn("Trainee with username: {} not found.", username);
            throw new NotFoundException("Trainee with username " + username + " not found.");
        }
        return trainee;
    }

    public boolean authenticate(String username, String password) {
        Optional<Trainee> trainee = traineeDAO.findByUserName(username);
        if (trainee.isEmpty()) {
            log.warn("Trainee with username: {} not found.", username);
            throw new NotFoundException("Trainee with username " + username + " not found.");
        }
        if (trainee.get().getUser().getPassword().equals(password)) {
            return true;
        } else {
            log.warn("Wrong password. Username: {} ", username);
            throw new NotAuthenticated("Wrong password. Username: " + username);
        }
    }

    public Optional<Trainee> updateActive(Long id, boolean active) {
        Optional<Trainee> traineeUpdated = traineeDAO.findById(id);
        if (traineeUpdated.isEmpty()) {
            log.warn("Trainee with ID: {} not found for active update.", id);
            throw new NotFoundException("Trainee with ID " + id + " not found for update.");
        }
        System.out.println(traineeUpdated.get());
        System.out.println(traineeUpdated.get().getUser());
        traineeUpdated.get().setUser(userService.updateActive(traineeUpdated.get().getUser().getId(), active).get());
        return traineeUpdated;
    }

    public Optional<Trainee> updatePassword(Long id, String password) {
        Optional<Trainee> traineeUpdated = traineeDAO.findById(id);
        if (traineeUpdated.isEmpty()) {
            log.warn("Trainee with ID: {} not found for password update.", id);
            throw new NotFoundException("Trainee with ID " + id + " not found for update.");
        }
        traineeUpdated.get().setUser(userService.updatePassword(traineeUpdated.get().getUser().getId(), password).get());
        return traineeUpdated;
    }

    public Optional<Trainee> updateTrainersList(Long traineeId, List<Trainer> trainers) {
        Optional<Trainee> traineeOptional = findById(traineeId);

        if (traineeOptional.isPresent()) {
            Trainee trainee = traineeOptional.get();

            // Save the updated trainee
            return save(trainee);
        } else {
            // Handle the case where the trainee is not found
            log.warn("Trainee with ID: {} not found for updating trainers list.", traineeId);
            throw new NotFoundException("Trainee with ID " + traineeId + " not found for updating trainers list.");
        }
    }


    public Trainee createTrainee(User user, String address, Date dateOfBirth) {

        if(user == null){
            log.warn("User is null.");
            throw new NullPointerException("User is null.");
        }
        if(address == null || address.isEmpty()){
            log.warn("Address is null.");
            throw new NullPointerException("Address is null.");
        }
        if(dateOfBirth == null){
            log.warn("Date of birth is null.");
            throw new NullPointerException("Date of birth is null.");
        }

        Trainee trainee = new Trainee();
        trainee.setAddress(address);
        trainee.setDateOfBirth(dateOfBirth);
        trainee.setUser(user);
        return trainee;
    }

    public Optional<Trainee> updateTrainers(Long traineeId, List<Trainer> trainers) {
//        if(trainers.isEmpty()) {
//            log.warn("Trainers list is empty.");
//            throw new NullPointerException("Trainers list is empty.");
//        }

        Optional<Trainee> traineeOptional = findById(traineeId);

        if (traineeOptional.isPresent()) {
            Trainee trainee = traineeOptional.get();
            trainers = new ArrayList<>(trainers);
            trainee.setTrainers(trainers);

            List<Training> trainings = trainingDAO.findByTrainee(traineeId).stream().flatMap(Optional::stream).toList();
            if(trainings.isEmpty() || trainings.contains(null)){
                log.warn("Trainings not found for trainee with ID: {}", traineeId);
                throw new NotFoundException("Trainings not found for trainee with ID: " + traineeId);
            }
            // Save the updated trainee
            return traineeDAO.updateTrainers(traineeId,  trainings, trainers);
        } else {
            // Handle the case where the trainee is not found
            log.warn("Trainee with ID: {} not found for updating trainers list.", traineeId);
            throw new NotFoundException("Trainee with ID " + traineeId + " not found for updating trainers list.");
        }
    }


    public List<Optional<Trainer>> findTrainersByTraineeId(Long id) {
        return traineeDAO.findTrainersByTraineeId(id);
    }

}
