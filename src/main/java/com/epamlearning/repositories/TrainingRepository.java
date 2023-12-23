package com.epamlearning.repositories;

import com.epamlearning.models.Training;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingRepository extends JpaRepository<Training, Long>, BaseRepository{
}
