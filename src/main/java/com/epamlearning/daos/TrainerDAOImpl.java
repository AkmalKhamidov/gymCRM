package com.epamlearning.daos;

import com.epamlearning.models.Trainee;
import com.epamlearning.models.Trainer;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class TrainerDAOImpl implements EntityDAO<Trainer> {

    private final SessionFactory sessionFactory;

    @Value("${FIND_NOT_ASSIGNED_ACTIVE_TRAINERS}")
    private String FIND_NOT_ASSIGNED_ACTIVE_TRAINERS;
    @Value("${FIND_ALL_TRAINERS_QUERY}")
    private String FIND_ALL_TRAINERS_QUERY;
    @Value("${FIND_TRAINER_BY_USERNAME_QUERY}")
    private String FIND_TRAINER_BY_USERNAME_QUERY;

    @Autowired
    public TrainerDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<Trainer> saveOrUpdate(Trainer trainer) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(trainer);
            session.getTransaction().commit();
            log.info("DAO: Trainer inserted/updated: {}", trainer);
            return Optional.ofNullable(trainer);
        }
    }

    @Override
    public Optional<Trainer> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {

            Trainer trainer = session.find(Trainer.class, id);
            log.info("DAO: Trainer found by id: {}. Trainer: {}", id, trainer);
            return Optional.ofNullable(trainer);

        } catch (HibernateException e) {
            log.warn("DAO: Error finding Trainer with ID: {}. Error: {}", id, e.getMessage());
            throw new RuntimeException("Error finding Trainer with ID " + id, e);
        }
    }

    @Override
    public List<Optional<Trainer>> findAll() {
        try (Session session = sessionFactory.openSession()) {
            List<Optional<Trainer>> trainers = session.createQuery(FIND_ALL_TRAINERS_QUERY, Trainer.class)
                    .getResultList()
                    .stream()
                    .map(Optional::ofNullable)
                    .toList();;
            log.info("DAO: Trainers found. Trainers: {}", trainers);
            return trainers;
        } catch (HibernateException e) {
            log.warn("DAO: Error finding all trainers. Error: {}", e.getMessage());
            throw new RuntimeException("Error finding all Trainers", e);
        }
    }

    @Override
    public void delete(Trainer trainer) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(trainer);
            session.getTransaction().commit();
            log.info("DAO: Trainer deleted. Trainer: {}", trainer);
        } catch (HibernateException e) {
            log.warn("DAO: Error delete Trainer: {}. Error: {}", trainer, e.getMessage());
            throw new RuntimeException("Error delete Trainer: " + trainer, e);
        }
    }

    @Override
    public Optional<Trainer> findByUserName(String username) {
        try (Session session = sessionFactory.openSession()) {
            Trainer trainer = session.createQuery(FIND_TRAINER_BY_USERNAME_QUERY, Trainer.class)
                    .setParameter(1, username)
                    .getSingleResultOrNull();
            log.info("DAO: Trainer found by username: {}. Trainer: {}", username, trainer);
            return Optional.ofNullable(trainer);
        } catch (HibernateException e) {
            log.warn("DAO: Error to find Trainer by username: {}. Error: {}", username, e.getMessage());
            throw new RuntimeException("Error to find Trainer by username: " + username, e);
        }
    }

    public List<Optional<Trainer>> findNotAssignedActiveTrainers(Trainee trainee) {
        try (Session session = sessionFactory.openSession()) {

            List<Optional<Trainer>> notAssignedActiveTrainers = session.createQuery(
                            FIND_NOT_ASSIGNED_ACTIVE_TRAINERS , Trainer.class)
                    .setParameter(1, trainee)
                    .getResultList()
                    .stream()
                    .map(Optional::ofNullable)
                    .toList();

            log.info("DAO: Not assigned active trainers found for Trainee: {}. Trainers: {}", trainee, notAssignedActiveTrainers);
            return notAssignedActiveTrainers;
        } catch (HibernateException e) {
            log.warn("DAO: Error finding not assigned active trainers for Trainee: {}. Error: {}", trainee, e.getMessage());
            throw new RuntimeException("Error finding not assigned active trainers for Trainee: " + trainee, e);
        }
    }

    public List<Optional<Trainee>> findTraineesByTrainer(Trainer trainer) {
        try (Session session = sessionFactory.openSession()) {
            session.refresh(trainer);
            return trainer.getTrainees().stream().map(Optional::ofNullable).toList();
        } catch (HibernateException e) {
            log.warn("DAO: Error finding all trainer's trainees. Error: {}", e.getMessage());
            throw new RuntimeException("Error finding all trainer's trainees", e);
        }
    }
}
