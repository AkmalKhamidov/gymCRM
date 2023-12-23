package com.epamlearning.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingTypeRepository extends JpaRepository<TrainingRepository, Long>, BaseRepository {
}
