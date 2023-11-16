package com.epamlearning.daos;

import com.epamlearning.models.Trainer;

import java.util.List;

public interface TrainerDAO {
    Trainer saveTrainer(Trainer trainer);

    Trainer updateTrainer(Trainer trainer);

    List<Trainer> findAllTrainers();

    Trainer findTrainerById(Long id);
}
