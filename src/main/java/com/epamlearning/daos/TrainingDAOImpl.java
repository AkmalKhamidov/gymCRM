package com.epamlearning.daos;

import com.epamlearning.models.Trainee;
import com.epamlearning.models.Trainer;
import com.epamlearning.models.Training;
import com.epamlearning.models.TrainingType;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


// Todo: move all queries to file and use @Value annotation to import them.
@Component
@Slf4j
public class TrainingDAOImpl implements EntityDAO<Training> {

    private SessionFactory sessionFactory;

    @Autowired
    public TrainingDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<Training> save(Training training) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(training); // session.save() is deprecated
            session.getTransaction().commit();
            log.info("DAO: Training created: {}", training);
            return Optional.ofNullable(training);
        }
    }

    @Override
    public Optional<Training> update(Long id, Training training) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Training trainingToUpdate = session.find(Training.class, id);
            trainingToUpdate.setTrainer(training.getTrainer());
            trainingToUpdate.setTrainee(training.getTrainee());
            trainingToUpdate.setTrainingType(training.getTrainingType());
            trainingToUpdate.setTrainingDate(training.getTrainingDate());
            trainingToUpdate.setTrainingDuration(training.getTrainingDuration());
            trainingToUpdate.setTrainingName(training.getTrainingName());
//            session.merge(traineeToUpdate);
            session.getTransaction().commit();
            log.info("DAO: Training updated. Id: {}, training: {}", id, trainingToUpdate);
            return Optional.ofNullable(trainingToUpdate);
        } catch (HibernateException e) {
            log.warn("Error updating Training with ID: {}. Error: {}", id, e.getMessage());
            throw new RuntimeException("Error updating Training with ID " + id, e);
        }
    }

    @Override
    public Optional<Training> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Training training = session.find(Training.class, id);
            log.info("DAO: Training found by ID. Training: {}", training);
            return Optional.ofNullable(training);
        } catch (HibernateException e) {
            log.warn("DAO: Error finding Training with ID: {}. Error: {}", id, e.getMessage());
            throw new RuntimeException("Error finding Training with ID " + id, e);
        }
    }

    @Override
    public List<Optional<Training>> findAll() {
        try (Session session = sessionFactory.openSession()) {
            List<Optional<Training>> trainings = session.createQuery("from Training ").getResultList();
            log.info("DAO: Trainings found. Trainings: {}", trainings);
            return trainings;
        } catch (HibernateException e) {
            log.warn("DAO: Error finding all trainings. Error: {}", e.getMessage());
            throw new RuntimeException("Error finding all Trainings", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Training trainingToDelete = session.find(Training.class, id);
            session.remove(trainingToDelete);
            session.getTransaction().commit();
            log.info("DAO: Training deleted. Id: {}, training: {}", id, trainingToDelete);
        } catch (HibernateException e) {
            log.warn("DAO: Error delete Training with ID: {}. Error: {}", id, e.getMessage());
            throw new RuntimeException("Error delete Training with ID " + id, e);
        }
    }

    @Override
    public Optional<Training> findByUserName(String username) {
        return Optional.empty();
    }

    public void deleteTrainingByTrainee(Long traineeId){
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createQuery("DELETE FROM Training t WHERE t.trainee.id=:traineeId")
                    .setParameter("traineeId", traineeId)
                    .executeUpdate();

            session.getTransaction().commit();
            log.info("DAO: Trainings deleted by trainee id: {}", traineeId);
        } catch (HibernateException e) {
            log.warn("DAO: Error to delete Trainings by trainee id: {}. Error: {}", traineeId, e.getMessage());
            throw new RuntimeException("Error to delete Trainings by trainee id: " + traineeId + ". ", e);
        }
    }


    public List<Optional<Training>> findByTrainee(Long traineeId) {
        try (Session session = sessionFactory.openSession()) {
            List<Optional<Training>> trainings = session.createQuery("SELECT t FROM Training t WHERE t.trainee.id=:traineeId", Training.class)
                    .setParameter("traineeId", traineeId)
                    .getResultList().stream().map(Optional::ofNullable).collect(Collectors.toList());

            log.info("DAO: Trainings found by trainee id: {}. Trainings: {}", traineeId, trainings);
            return trainings;

        } catch (HibernateException e) {
            log.warn("DAO: Error to find Training by trainee id: {}. Error: {}", traineeId, e.getMessage());
            throw new RuntimeException("Error to find Training by trainee id: " + traineeId + ". ", e);
        }
    }

    public List<Optional<Training>> findByTraineeAndCriteria(Trainee trainee, Date dateFrom, Date dateTo, TrainingType trainingType) {
        try (Session session = sessionFactory.openSession()) {

            CriteriaBuilder cb = sessionFactory.getCriteriaBuilder();
            CriteriaQuery<Training> cq = cb.createQuery(Training.class);
            Root<Training> t = cq.from(Training.class);
            cq.select(t).where(cb.equal(t.get("trainee"), trainee), cb.between(t.get("trainingDate"), dateFrom, dateTo), cb.equal(t.get("trainingType"), trainingType));
            List<Optional<Training>> trainings = session.createQuery(cq).getResultList().stream().map(Optional::ofNullable).collect(Collectors.toList());

            log.info("DAO: Trainings found by trainee and criteria: trainee: {}, dateFrom: {}, dateTo: {}, trainingType: {}. Trainings: {}", trainee, dateFrom, dateTo, trainingType, trainings);
            return trainings;

        } catch (HibernateException e) {
            log.warn("DAO: Error to find Training by trainee and criteria: trainee: {}, dateFrom: {}, dateTo: {}, trainingType: {}. Error: {}", trainee, dateFrom, dateTo, trainingType, e.getMessage());
            throw new RuntimeException("Error to find Training by trainee and criteria: trainee: " + trainee + ", dateFrom: " + dateFrom + ", dateTo: " + dateTo + ", trainingType: " + trainingType + ". ", e);
        }
    }

    public List<Optional<Training>> findByTrainerAndCriteria(Trainer trainer, Date dateFrom, Date dateTo, TrainingType trainingType) {
        try (Session session = sessionFactory.openSession()) {

            CriteriaBuilder cb = sessionFactory.getCriteriaBuilder();
            CriteriaQuery<Training> cq = cb.createQuery(Training.class);
            Root<Training> t = cq.from(Training.class);
            cq.select(t).where(cb.equal(t.get("trainer"), trainer), cb.between(t.get("trainingDate"), dateFrom, dateTo), cb.equal(t.get("trainingType"), trainingType));
            List<Optional<Training>> trainings = session.createQuery(cq).getResultList().stream().map(Optional::ofNullable).collect(Collectors.toList());

            log.info("DAO: Trainings found by trainer and criteria: trainer: {}, dateFrom: {}, dateTo: {}, trainingType: {}. Trainings: {}", trainer, dateFrom, dateTo, trainingType, trainings);
            return trainings;

        } catch (HibernateException e) {
            log.warn("DAO: Error to find Training by trainee and criteria: trainer: {}, dateFrom: {}, dateTo: {}, trainingType: {}. Error: {}", trainer, dateFrom, dateTo, trainingType, e.getMessage());
            throw new RuntimeException("Error to find Training by trainee and criteria: trainer: " + trainer + ", dateFrom: " + dateFrom + ", dateTo: " + dateTo + ", trainingType: " + trainingType + ". ", e);
        }
    }

    public List<Optional<Trainer>> findTrainersByTrainee(Long traineeId) {
        try (Session session = sessionFactory.openSession()) {

            List<Optional<Trainer>> trainers = session.createQuery("SELECT t.trainer FROM Training t WHERE t.trainee.id=:traineeId", Trainer.class)
                    .setParameter("traineeId", traineeId)
                    .getResultList().stream().map(Optional::ofNullable).collect(Collectors.toList());

            log.info("DAO: Trainers found by trainee id: {}. Trainers: {}", traineeId, trainers);
            return trainers;

        } catch (HibernateException e) {
            log.warn("DAO: Error to find Trainers by trainee Id: {}. Error: {}", traineeId, e.getMessage());
            throw new RuntimeException("Error to find Trainers by trainee Id: " + traineeId + ". ", e);
        }
    }

    public Training updateTrainingTrainer(Long trainingId, Trainer trainer) {
       try(Session session = sessionFactory.openSession()) {
           session.beginTransaction();
           Training training = session.find(Training.class, trainingId);
           training.setTrainer(trainer);
           session.getTransaction().commit();
           log.info("DAO: Training updated. Id: {}, training: {}", trainingId, training);
           return training;
       } catch (HibernateException e) {
           log.warn("DAO: Error to update Training with ID: {}. Error: {}", trainingId, e.getMessage());
           throw new RuntimeException("Error to update Training with ID " + trainingId, e);
       }
    }

}
