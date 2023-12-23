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
public class UserService implements BaseService<User> {

    private final UserDAOImpl userDAO;

    @Autowired
    public UserService(UserDAOImpl userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public Optional<User> save(User user) {
        return userDAO.saveOrUpdate(user);
    }

    @Override
    public Optional<User> update(Long id, User user) {

        if (id == null) {
            log.warn("ID is null.");
            throw new NullPointerException("ID is null.");
        }

        userNullVerification(user);

        Optional<User> userUpdatedOptional = userDAO.findById(id);
        if (userUpdatedOptional.isPresent()) {
            User userUpdated = userUpdatedOptional.get();
            userUpdated.setFirstName(user.getFirstName());
            userUpdated.setLastName(user.getLastName());
            userUpdated.setUsername(user.getUsername());
            userUpdated.setPassword(user.getPassword());
            userUpdated.setActive(user.isActive());
            return userDAO.saveOrUpdate(userUpdated);
        } else {
            log.warn("User with ID: {} not found for update.", id);
            throw new NotFoundException("User with ID " + id + " not found for update.");
        }
    }

    public Optional<User> updatePassword(Long id, String password) {

        if (id == null) {
            log.warn("ID is null.");
            throw new NullPointerException("ID is null.");
        }
        if (password == null || password.isEmpty()) {
            log.warn("Password is null.");
            throw new NullPointerException("Password is null.");
        }

        Optional<User> userUpdatedOptional = userDAO.findById(id);
        if (userUpdatedOptional.isPresent()) {
            User userUpdated = userUpdatedOptional.get();
            userUpdated.setPassword(password);
            return userDAO.saveOrUpdate(userUpdated);
        } else {
            log.warn("User with ID: {} not found for password update.", id);
            throw new NotFoundException("User with ID " + id + " not found for update.");
        }
    }

    public Optional<User> updateActive(Long id, boolean active) {

        if (id == null) {
            log.warn("ID is null.");
            throw new NullPointerException("ID is null.");
        }

        Optional<User> userUpdatedOptional = userDAO.findById(id);
        if (userUpdatedOptional.isPresent()) {
            User userUpdated = userUpdatedOptional.get();
            userUpdated.setActive(active);
            return userDAO.saveOrUpdate(userUpdated);
        } else {
            log.warn("User with ID: {} not found for active update.", id);
            throw new NotFoundException("User with ID " + id + " not found for update.");
        }
    }

    @Override
    public Optional<User> findById(Long id) {

        if (id == null) {
            log.warn("ID is null.");
            throw new NullPointerException("ID is null.");
        }

        Optional<User> user = userDAO.findById(id);
        if (user.isEmpty()) {
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

        if (id == null) {
            log.warn("ID is null.");
            throw new NullPointerException("ID is null.");
        }

        Optional<User> user = userDAO.findById(id);
        if (user.isPresent()) {
            userDAO.delete(user.get());
        } else {
            log.warn("User with ID: {} not found for delete.", id);
            throw new NotFoundException("User with ID " + id + " not found for delete.");
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        if (username == null || username.isEmpty()) {
            log.warn("Username is null.");
            throw new NullPointerException("Username is null.");
        }
        Optional<User> user = userDAO.findByUserName(username);
        if (user.isEmpty()) {
            log.warn("User with username: {} not found.", username);
            throw new NotFoundException("User with username " + username + " not found.");
        }
        return user;
    }

    public Long authenticate(String username, String password) {
        if (username == null || username.isEmpty()) {
            log.warn("Username is null.");
            throw new NullPointerException("Username is null.");
        }
        if (password == null || password.isEmpty()) {
            log.warn("Password is null.");
            throw new NullPointerException("Password is null.");
        }

        Optional<User> user = userDAO.findByUserName(username);
        if (user.isEmpty()) {
            log.warn("User with username: {} not found.", username);
            throw new NotFoundException("User with username " + username + " not found.");
        }
        if (user.get().getPassword().equals(password)) {
            return user.get().getId();
        } else {
            log.warn("Wrong password. Username: {} ", username);
            throw new NotAuthenticated("Wrong password. Username: " + username);
        }
    }

    public User createUser(String firstName, String lastName) {

        if (firstName == null || firstName.isEmpty()) {
            log.warn("First name is null.");
            throw new NullPointerException("First name is null.");
        }
        if (lastName == null || lastName.isEmpty()) {
            log.warn("Last name is null.");
            throw new NullPointerException("Last name is null.");
        }

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
        return password.toString();
    }

    public void userNullVerification(User user) {
        if (user == null) {
            log.warn("User is null.");
            throw new NullPointerException("User is null.");
        }
        if (user.getFirstName() == null || user.getFirstName().isEmpty()) {
            log.warn("First name is null.");
            throw new NullPointerException("First name is null.");
        }
        if (user.getLastName() == null || user.getLastName().isEmpty()) {
            log.warn("Last name is null.");
            throw new NullPointerException("Last name is null.");
        }
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            log.warn("Username is null.");
            throw new NullPointerException("Username is null.");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            log.warn("Password is null.");
            throw new NullPointerException("Password is null.");
        }
    }

}
