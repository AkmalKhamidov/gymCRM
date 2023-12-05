package com.epamlearning.services;

import com.epamlearning.daos.UserDAOImpl;
import com.epamlearning.exceptions.NotAuthenticated;
import com.epamlearning.exceptions.NotFoundException;
import com.epamlearning.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Slf4j
public class UserService implements EntityService<User>{

    private UserDAOImpl userDAO;

    @Autowired
    public void setTraineeDAO(UserDAOImpl userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public Optional<User> save(User user) {
        return userDAO.save(user);
    }

    @Override
    public Optional<User> update(Long id, User user) {
        Optional<User> userUpdated = userDAO.update(id, user);
        if (userUpdated.isEmpty()) {
            log.warn("User with ID: {} not found for update.", id);
            throw new NotFoundException("User with ID " + id + " not found for update.");
        }
        return userUpdated;
    }

    public Optional<User> updatePassword(Long id, String password) {
        Optional<User> userUpdated = userDAO.updatePassword(id, password);
        if (userUpdated.isEmpty()) {
            log.warn("User with ID: {} not found for password update.", id);
            throw new NotFoundException("User with ID " + id + " not found for update.");
        }
        return userUpdated;
    }

    public Optional<User> updateActive(Long id, boolean active) {
        Optional<User> userUpdated = userDAO.updateActive(id, active);
        if (userUpdated.isEmpty()) {
            log.warn("User with ID: {} not found for active update.", id);
            throw new NotFoundException("User with ID " + id + " not found for update.");
        }
        return userUpdated;
    }

    @Override
    public Optional<User> findById(Long id) {
        Optional<User> user = userDAO.findById(id);
        if(user.isEmpty()) {
            log.warn("User with ID: {} not found.", id);
            throw new NotFoundException("User with ID " + id + " not found.");
        }
        return user;
    }

    @Override
    public List<Optional<User>> findAll() {
        return userDAO.findAll();
    }

    @Override
    public void deleteById(Long id) {
        Optional<User> user = userDAO.findById(id);
        if(user.isEmpty()){
            log.warn("User with ID: {} not found for delete.", id);
            throw new NotFoundException("User with ID " + id + " not found for delete.");
        } else {
            userDAO.deleteById(id);
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        Optional<User> user = userDAO.findByUserName(username);
        if(user.isEmpty()){
            log.warn("User with username: {} not found.", username);
            throw new NotFoundException("User with username " + username + " not found.");
        }
        return user;
    }

    public boolean authenticate(String username, String password) {
        Optional<User> user = userDAO.findByUserName(username);
        if(user.isEmpty()){
            log.warn("User with username: {} not found.", username);
            throw new NotFoundException("User with username " + username + " not found.");
        }
        if(user.get().getPassword().equals(password)){
            return true;
        } else {
            log.warn("Wrong password. Username: {} ",username);
            throw new NotAuthenticated("Wrong password. Username: " + username);
        }
    }

    public User createUser(String firstName, String lastName) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(generateUserName(firstName, lastName));
        user.setPassword(generateRandomPassword());
        user.setActive(true);
        return user;
    }

    public String generateUserName(String firstName, String lastName) {
        String baseUsername = firstName + "." + lastName;
        String username = baseUsername;
        int serialNumber = 1;

        while (userDAO.findByUserName(username).isPresent()) {
            username = baseUsername + serialNumber;
            serialNumber++;
        }
        log.info("User's username generated: {}, firstName: {}, lastName: {}", username, firstName, lastName);
        return username;
    }

    public String generateRandomPassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(characters.length());
            password.append(characters.charAt(index));
        }
//        log.info("User's password generated: {}", password);
        return password.toString();
    }

}
