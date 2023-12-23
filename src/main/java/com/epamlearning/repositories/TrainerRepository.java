package com.epamlearning.repositories;

import com.epamlearning.models.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainerRepository extends JpaRepository<Trainer, Long>, BaseRepository{
}
