package com.epamlearning.storage;

import com.epamlearning.models.*;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Getter
public class InMemoryStorage {
    private final Map<Long, Trainee> storageTrainees = new HashMap<>();
    private final Map<Long, Trainer> storageTrainers = new HashMap<>();
    private final Map<Long, Training> storageTrainings = new HashMap<>();
    private final Map<Long, User> storageUsers = new HashMap<>();
    private final Map<Long, TrainingType> storageTrainingTypes = new HashMap<>();
}
