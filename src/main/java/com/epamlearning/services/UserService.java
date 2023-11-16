package com.epamlearning.services;

import com.epamlearning.models.User;
import com.epamlearning.storage.InMemoryStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@Slf4j
public class UserService {

    private InMemoryStorage inMemoryStorage;

    @Autowired
    public void setInMemoryStorage(InMemoryStorage inMemoryStorage) {
        this.inMemoryStorage = inMemoryStorage;
    }

    public User createUser(String firstName, String lastName) {
        final String username = generateUserName(firstName, lastName);
        final String password = generateRandomPassword();

        User user = new User();
        user.setId(generateNewUserId());
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setPassword(password);
        user.setActive(true);
        log.info("User created: {}", user);
        return user;
    }

    public String generateUserName(String firstName, String lastName) {
        String baseUsername = firstName + "." + lastName;
        String username = baseUsername;
        int serialNumber = 1;

        while (checkForExistingUsers(username)) {
            username = baseUsername + serialNumber;
            serialNumber++;
        }
        log.info("User's username generated: {}, firstName: {}, lastName: {}", username, firstName, lastName);
        return username;
    }

    private long generateNewUserId() {
        long maxId = inMemoryStorage.getStorageUsers().values().stream()
                .mapToLong(User::getId)
                .max()
                .orElse(0);
        return maxId + 1;
    }

    public String generateRandomPassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(characters.length());
            password.append(characters.charAt(index));
        }
        log.info("User's password generated: {}", password);
        return password.toString();
    }

    private boolean checkForExistingUsers(String username) {
        return inMemoryStorage.getStorageTrainees().values().stream()
                .anyMatch(trainee -> {
                    User user = trainee.getUser();
                    return user != null && username.equals(user.getUsername());
                })
                || inMemoryStorage.getStorageTrainers().values().stream()
                .anyMatch(trainer -> {
                    User user = trainer.getUser();
                    return user != null && username.equals(user.getUsername());
                });
    }
}
