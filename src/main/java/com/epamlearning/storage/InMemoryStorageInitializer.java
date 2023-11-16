package com.epamlearning.storage;

import com.epamlearning.models.Trainee;
import com.epamlearning.models.Trainer;
import com.epamlearning.models.Training;
import com.epamlearning.models.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
public class InMemoryStorageInitializer implements BeanPostProcessor {

    private final InMemoryStorage inMemoryStorage;

    @Value("${data.file.path}")
    private String filePath;

    @Autowired
    public InMemoryStorageInitializer(InMemoryStorage inMemoryStorage) {
        this.inMemoryStorage = inMemoryStorage;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        ObjectMapper objectMapper = new ObjectMapper();
        File jsonFile = new File(filePath);
        try {
            log.info(" --- Loading data from file --- ");

            HashMap<String, Object> data = objectMapper.readValue(jsonFile, new TypeReference<>() {});

            List<User> users = objectMapper.convertValue(data.get("users"), new TypeReference<>(){});
            users.forEach(user -> inMemoryStorage.getStorageUsers().put(user.getId(), user));
            log.info("Users: {}", users);

            List<Trainee> trainees = objectMapper.convertValue(data.get("trainees"), new TypeReference<>(){});
            trainees.forEach(trainee -> inMemoryStorage.getStorageTrainees().put(trainee.getId(), trainee));
            log.info("Trainees: {}", trainees);

            List<Trainer> trainers = objectMapper.convertValue(data.get("trainers"), new TypeReference<>() {});
            trainers.forEach(trainer -> inMemoryStorage.getStorageTrainers().put(trainer.getId(), trainer));
            log.info("Trainers: {}", trainers);

            List<Training> trainings = objectMapper.convertValue(data.get("trainings"), new TypeReference<>() {});
            trainings.forEach(training -> inMemoryStorage.getStorageTrainings().put(training.getId(), training));
            log.info("Trainings: {}", trainings);

            log.info(" --- Loading data finished --- ");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bean;
    }
}
