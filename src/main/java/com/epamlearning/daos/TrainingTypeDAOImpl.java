package com.epamlearning.daos;

import com.epamlearning.models.TrainingType;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TrainingTypeDAOImpl implements EntityDAO<TrainingType> {

    private final SessionFactory sessionFactory;

    @Value("${FIND_ALL_TRAINING_TYPES_QUERY}")
    private String FIND_ALL_TRAINING_TYPES_QUERY;

    @Autowired
    public TrainingTypeDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<TrainingType> saveOrUpdate(TrainingType trainingType) {
        return Optional.empty();
    }

    @Override
    public Optional<TrainingType> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            TrainingType trainingType = session.find(TrainingType.class, id);
            log.info("DAO: TrainingType found. TrainingType: {}", trainingType);
            return Optional.ofNullable(trainingType);
        } catch (HibernateException e) {
            log.warn("Error finding TrainingTypes with ID: {}. Error: {}", id, e.getMessage());
            throw new RuntimeException("Error finding TrainingTypes with ID " + id, e);
        }
    }

    @Override
    public List<Optional<TrainingType>> findAll() {
        try (Session session = sessionFactory.openSession()) {
            List<Optional<TrainingType>> trainees = session.createQuery(FIND_ALL_TRAINING_TYPES_QUERY, TrainingType.class)
                    .getResultList()
                    .stream()
                    .map(Optional::ofNullable)
                    .collect(Collectors.toList());
            log.info("DAO: TrainingTypes found. TrainingTypes: {}", trainees);
            return trainees;
        } catch (HibernateException e) {
            log.warn("DAO: Error finding all training types. Error: {}", e.getMessage());
            throw new RuntimeException("Error finding all TrainingTypes", e);
        }
    }

    @Override
    public void delete(TrainingType trainingType) {
    }

    @Override
    public Optional<TrainingType> findByUserName(String username) {
        return Optional.empty();
    }
}
