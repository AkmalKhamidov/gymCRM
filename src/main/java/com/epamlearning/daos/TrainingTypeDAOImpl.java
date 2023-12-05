package com.epamlearning.daos;

import com.epamlearning.models.TrainingType;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class TrainingTypeDAOImpl implements EntityDAO<TrainingType>{

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Optional<TrainingType> save(TrainingType trainingType) {
        return Optional.empty();
    }

    @Override
    public Optional<TrainingType> update(Long id, TrainingType trainingType) {
        return Optional.empty();
    }

    @Override
    public Optional<TrainingType> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            TrainingType trainingType = session.find(TrainingType.class, id);
            session.getTransaction().commit();
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
            session.beginTransaction();
            List<Optional<TrainingType>> trainees = session.createQuery("from TrainingType").getResultList();
            session.getTransaction().commit();
            log.info("DAO: TrainingTypes found. TrainingTypes: {}", trainees);
            return trainees;
        } catch (HibernateException e) {
            log.warn("DAO: Error finding all training types. Error: {}", e.getMessage());
            throw new RuntimeException("Error finding all TrainingTypes", e);
        }
    }

    @Override
    public void deleteById(Long id) {
    }

    @Override
    public Optional<TrainingType> findByUserName(String username) {
        return Optional.empty();
    }
}
