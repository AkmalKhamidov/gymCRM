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
public class TraineeDAOImpl implements EntityDAO<Trainee> {

    private final SessionFactory sessionFactory;

    @Value("${FIND_ALL_TRAINEES_QUERY}")
    private String FIND_ALL_TRAINEES_QUERY;
//    private String FIND_TRAINEE_TRAINERS_QUERY = ;
    private String FIND_TRAINEE_BY_USERNAME_QUERY = "SELECT t FROM Trainee t WHERE t.user.username = ?1";

    @Autowired
    public TraineeDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<Trainee> saveOrUpdate(Trainee trainee) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(trainee);
            session.getTransaction().commit();
            log.info("DAO: Trainee inserted/updated. Trainee: {}", trainee);
            return Optional.ofNullable(trainee);
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
            List<Optional<Trainee>> trainees = session.createQuery(FIND_ALL_TRAINEES_QUERY, Trainee.class)
                    .getResultList()
                    .stream()
                    .map(Optional::ofNullable)
                    .toList();
            log.info("DAO: Trainees found. Trainees: {}", trainees);
            return trainees;
        } catch (HibernateException e) {
            log.warn("DAO: Error finding all trainees. Error: {}", e.getMessage());
            throw new RuntimeException("Error finding all Trainees", e);
        }
    }

    public List<Optional<Trainer>> findTrainersByTrainee(Trainee trainee) {
        try (Session session = sessionFactory.openSession()) {
            session.refresh(trainee);
            return trainee.getTrainers().stream().map(Optional::ofNullable).toList();
        } catch (HibernateException e) {
            log.warn("DAO: Error finding all trainee's trainers. Error: {}", e.getMessage());
            throw new RuntimeException("Error finding all trainee's trainers", e);
        }
    }

    @Override
    public void delete(Trainee trainee) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(trainee);
            session.getTransaction().commit();
            log.info("Trainee deleted. trainee: {}", trainee);
        } catch (HibernateException e) {
            log.warn("Error delete Trainee: {}. Error: {}", trainee, e.getMessage());
            throw new RuntimeException("Error delete Trainee: " + trainee, e);
        }
    }

    @Override
    public Optional<Trainee> findByUserName(String username) {
        try(Session session = sessionFactory.openSession()) {
            Trainee trainee = session.createQuery(FIND_TRAINEE_BY_USERNAME_QUERY, Trainee.class)
                    .setParameter(1, username)
                    .getSingleResultOrNull();
            log.info("Trainee found by username: {}. Trainee: {}", username, trainee);
            return Optional.ofNullable(trainee);
        } catch (HibernateException e) {
            log.warn("Error to find Trainee by username: {}. Error: {}", username, e.getMessage());
            throw new RuntimeException("Error to find Trainee by username " + username, e);
        }
    }

}
