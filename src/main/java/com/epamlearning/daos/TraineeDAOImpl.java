package com.epamlearning.daos;

import com.epamlearning.models.Trainee;
import com.epamlearning.models.Trainer;
import com.epamlearning.models.Training;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// TODO: remove transaction for read (select)

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TraineeDAOImpl implements EntityDAO<Trainee> {

    private SessionFactory sessionFactory;

    @Autowired
    public TraineeDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<Trainee> save(Trainee trainee) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(trainee); // session.save() is deprecated
            session.getTransaction().commit();
            log.info("DAO: Trainee inserted. Trainee: {}", trainee);
            return Optional.ofNullable(trainee);
        }
    }

    @Override
    public Optional<Trainee> update(Long id, Trainee trainee) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Trainee traineeToUpdate = session.find(Trainee.class, id);
            traineeToUpdate.setUser(trainee.getUser());
            traineeToUpdate.setAddress(trainee.getAddress());
            traineeToUpdate.setDateOfBirth(trainee.getDateOfBirth());
            session.merge(traineeToUpdate);
            session.getTransaction().commit();
            log.info("DAO: Trainee updated. Id: {}, trainee: {}", id, traineeToUpdate);
            return Optional.ofNullable(traineeToUpdate);
        } catch (HibernateException e) {
            log.warn("Error updating Trainee with ID: {}. Error: {}", id, e.getMessage());
            throw new RuntimeException("Error updating Trainee with ID " + id, e);
        }
    }

    @Override
    public Optional<Trainee> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Trainee trainee = session.find(Trainee.class, id);
            log.info("DAO: Trainee found. Trainee: {}", trainee);
            return Optional.ofNullable(trainee);
        } catch (HibernateException e) {
            log.warn("Error finding Trainee with ID: {}. Error: {}", id, e.getMessage());
            throw new RuntimeException("Error finding Trainee with ID " + id, e);
        }
    }

    @Override
    public List<Optional<Trainee>> findAll() {
        try (Session session = sessionFactory.openSession()) {
            List<Optional<Trainee>> trainees = session.createQuery("from Trainee t").getResultList();
            log.info("DAO: Trainees found. Trainees: {}", trainees);
            return trainees;
        } catch (HibernateException e) {
            log.warn("DAO: Error finding all trainees. Error: {}", e.getMessage());
            throw new RuntimeException("Error finding all Trainees", e);
        }
    }

    public List<Optional<Trainer>> findTrainersByTraineeId(Long id) {
        try (Session session = sessionFactory.openSession()) {
            List<Optional<Trainer>> trainers = session.createQuery("SELECT t.trainers FROM Trainee t WHERE t.id=:value", Trainer.class)
                    .setParameter("value", id)
                    .getResultList().stream().map(Optional::ofNullable).collect(Collectors.toList());
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
            Trainee traineeToDelete = session.find(Trainee.class, id);
            session.remove(traineeToDelete);
            session.getTransaction().commit();
            log.info("Trainee deleted. Id: {}, trainee: {}", id, traineeToDelete);
        } catch (HibernateException e) {
            log.warn("Error delete Trainee with ID: {}. Error: {}", id, e.getMessage());
            throw new RuntimeException("Error delete Trainee with ID " + id, e);
        }
    }

    @Override
    public Optional<Trainee> findByUserName(String username) {
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Trainee trainee = session.createQuery("SELECT t FROM Trainee t WHERE t.user.username=:value" , Trainee.class)
                    .setParameter("value", username)
                    .getSingleResultOrNull();
            log.info("Trainee found by username: {}. Trainee: {}", username, trainee);
            session.getTransaction().commit();
            return Optional.ofNullable(trainee);
        } catch (HibernateException e) {
            log.warn("Error to find Trainee by username: {}. Error: {}", username, e.getMessage());
            throw new RuntimeException("Error to find Trainee by username " + username, e);
        }
    }

    public Optional<Trainee> updateTrainers(Long traineeId, List<Training> trainings, List<Trainer> trainers) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Trainee trainee = session.find(Trainee.class, traineeId);

            // Update trainerId for each associated training
            for (Training training : trainings) {
                // Assuming trainers list is not empty and you want to update the first trainer
                if (!trainers.isEmpty()) {
                    training.setTrainer(trainers.get(0));
                } else {
                    // Handle the case where trainers list is empty, you may want to set it to null or another default value
                    training.setTrainer(null); // Set it to null for example
                }
                session.merge(training);
            }

            trainee.setTrainers(trainers);
            session.merge(trainee);
            session.getTransaction().commit();
            log.info("DAO: Trainee updated. Trainee: {}", trainee);
            return Optional.ofNullable(trainee);
        } catch (HibernateException e) {
            log.warn("Error updating Trainee ID: {}. Error: {}", traineeId, e.getMessage());
            throw new RuntimeException("Error updating Trainee ID: {}" + traineeId, e);
        }
    }

}
