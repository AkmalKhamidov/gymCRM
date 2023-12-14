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
import java.util.stream.Collectors;

@Component
@Slf4j
public class TrainerDAOImpl implements EntityDAO<Trainer> {

    private SessionFactory sessionFactory;

    @Value("${findNotAssignedActiveTrainers}")
    private String findNotAssignedActiveTrainersQuery;

    @Autowired
    public TrainerDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<Trainer> save(Trainer trainer) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(trainer); // session.save() is deprecated
            session.getTransaction().commit();
            log.info("DAO: Trainer inserted: {}", trainer);
            return Optional.ofNullable(trainer);
        }
    }

    @Override
    public Optional<Trainer> update(Long id, Trainer trainer) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Trainer trainerToUpdate = session.find(Trainer.class, id);
            trainerToUpdate.setUser(trainer.getUser());
            trainerToUpdate.setSpecialization(trainer.getSpecialization());
                session.merge(trainerToUpdate);
            session.getTransaction().commit();
            log.info("DAO: Trainer updated: {}", trainer);
            return Optional.ofNullable(trainerToUpdate);
        } catch (HibernateException e) {
            log.warn("DAO: Error updating Trainer with ID: {}. Error: {}", id, e.getMessage());
            throw new RuntimeException("Error updating Trainer with ID " + id, e);
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
            List<Optional<Trainer>> trainers = session.createQuery("from Trainer").getResultList();
            log.info("DAO: Trainers found. Trainers: {}", trainers);
            return trainers;
        } catch (HibernateException e) {
            log.warn("DAO: Error finding all trainers. Error: {}", e.getMessage());
            throw new RuntimeException("Error finding all Trainers", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Trainer trainerToDelete = session.find(Trainer.class, id);
            session.remove(trainerToDelete);
            session.getTransaction().commit();
            log.info("DAO: Trainer deleted. Id: {}, trainer: {}", id, trainerToDelete);
        } catch (HibernateException e) {
            log.warn("DAO: Error delete Trainer with ID: {}. Error: {}", id, e.getMessage());
            throw new RuntimeException("Error delete Trainer with ID " + id, e);
        }
    }

    @Override
    public Optional<Trainer> findByUserName(String username) {
        try (Session session = sessionFactory.openSession()) {
            Trainer trainer = session.createQuery("SELECT t FROM Trainer t WHERE t.user.username=:value", Trainer.class)
                    .setParameter("value", username)
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
                            findNotAssignedActiveTrainersQuery, Trainer.class)
                    .setParameter(1, trainee)
                    .getResultList()
                    .stream()
                    .map(Optional::ofNullable)
                    .collect(Collectors.toList());

            log.info("DAO: Not assigned active trainers found for Trainee: {}. Trainers: {}", trainee, notAssignedActiveTrainers);
            return notAssignedActiveTrainers;
        } catch (HibernateException e) {
            log.warn("DAO: Error finding not assigned active trainers for Trainee: {}. Error: {}", trainee, e.getMessage());
            throw new RuntimeException("Error finding not assigned active trainers for Trainee: " + trainee, e);
        }
    }
}
