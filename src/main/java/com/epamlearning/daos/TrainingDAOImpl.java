package com.epamlearning.daos;

import com.epamlearning.models.Trainee;
import com.epamlearning.models.Trainer;
import com.epamlearning.models.Training;
import com.epamlearning.models.TrainingType;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class TrainingDAOImpl implements EntityDAO<Training> {

    private final SessionFactory sessionFactory;

    @Value("${FIND_ALL_TRAININGS_QUERY}")
    private String FIND_ALL_TRAININGS_QUERY;
    @Value("${FIND_TRAININGS_BY_TRAINEE_QUERY}")
    private String FIND_TRAINING_BY_TRAINEE_QUERY;
    @Value("${DELETE_TRAININGS_BY_TRAINEE_QUERY}")
    private String DELETE_TRAINING_BY_TRAINEE_QUERY;
    @Value("${FIND_TRAININGS_BY_TRAINER_QUERY}")
    private String FIND_TRAININGS_BY_TRAINER_QUERY;
    @Value("${FIND_TRAINERS_BY_TRAINEE_QUERY}")
    private String FIND_TRAINERS_BY_TRAINEE_QUERY;
    @Value("${FIND_ANOTHER_TRAINING_BY_TRAINER_AND_TRAINEE_QUERY}")
    private String FIND_ANOTHER_TRAINING_BY_TRAINER_AND_TRAINEE_QUERY;
    @Value("${FIND_TRAININGS_BY_TRAINER_AND_TRAINEE_QUERY}")
    private String FIND_TRAININGS_BY_TRAINER_AND_TRAINEE_QUERY;

    @Autowired
    public TrainingDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<Training> saveOrUpdate(Training training) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(training);
            session.getTransaction().commit();
            log.info("DAO: Training inserted/updated: {}", training);
            return Optional.ofNullable(training);
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
            List<Optional<Training>> trainings = session.createQuery(FIND_ALL_TRAININGS_QUERY, Training.class)
                    .getResultList()
                    .stream()
                    .map(Optional::ofNullable)
                    .toList();
            log.info("DAO: Trainings found. Trainings: {}", trainings);
            return trainings;
        } catch (HibernateException e) {
            log.warn("DAO: Error finding all trainings. Error: {}", e.getMessage());
            throw new RuntimeException("Error finding all Trainings", e);
        }
    }

    @Override
    public void delete(Training training) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(training);
            session.getTransaction().commit();
            log.info("DAO: Training deleted. Training: {}", training);
        } catch (HibernateException e) {
            log.warn("DAO: Error delete Training: {}. Error: {}", training, e.getMessage());
            throw new RuntimeException("Error delete Training: " + training, e);
        }
    }

    @Override
    public Optional<Training> findByUserName(String username) {
        return Optional.empty();
    }

    public void deleteTrainingByTrainee(Trainee trainee) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createQuery(DELETE_TRAINING_BY_TRAINEE_QUERY)
                    .setParameter(1, trainee)
                    .executeUpdate();
            session.getTransaction().commit();
            log.info("DAO: Trainings deleted by trainee: {}", trainee);
        } catch (HibernateException e) {
            log.warn("DAO: Error to delete Trainings by trainee: {}. Error: {}", trainee, e.getMessage());
            throw new RuntimeException("Error to delete Trainings by trainee: " + trainee + ". ", e);
        }
    }

    public List<Optional<Training>> findByTrainee(Trainee trainee) {
        try (Session session = sessionFactory.openSession()) {
            List<Optional<Training>> trainings = session.createQuery(FIND_TRAINING_BY_TRAINEE_QUERY, Training.class)
                    .setParameter(1, trainee)
                    .getResultList()
                    .stream()
                    .map(Optional::ofNullable)
                    .toList();

            log.info("DAO: Trainings found by trainee: {}. Trainings: {}", trainee, trainings);
            return trainings;

        } catch (HibernateException e) {
            log.warn("DAO: Error to find Training by trainee: {}. Error: {}", trainee, e.getMessage());
            throw new RuntimeException("Error to find Training by trainee: " + trainee + ". ", e);
        }
    }

    public List<Optional<Training>> findByTrainer(Trainer trainer) {
        try (Session session = sessionFactory.openSession()) {
            List<Optional<Training>> trainings = session.createQuery(FIND_TRAININGS_BY_TRAINER_QUERY, Training.class)
                    .setParameter(1, trainer)
                    .getResultList()
                    .stream()
                    .map(Optional::ofNullable)
                    .toList();

            log.info("DAO: Trainings found by trainer: {}. Trainings: {}", trainer, trainings);
            return trainings;

        } catch (HibernateException e) {
            log.warn("DAO: Error to find Training by trainer: {}. Error: {}", trainer, e.getMessage());
            throw new RuntimeException("Error to find Training by trainer: " + trainer + ". ", e);
        }
    }

    public List<Optional<Training>> findByTraineeAndCriteria(Trainee trainee, Date dateFrom, Date dateTo, TrainingType trainingType) {
        try (Session session = sessionFactory.openSession()) {

            CriteriaBuilder cb = sessionFactory.getCriteriaBuilder();
            CriteriaQuery<Training> cq = cb.createQuery(Training.class);
            Root<Training> t = cq.from(Training.class);

            List<Predicate> predicates = new ArrayList<>();

            // Conditions for date range
            if (dateFrom != null && dateTo == null) {
                predicates.add(cb.greaterThanOrEqualTo(t.get("trainingDate"), dateFrom));
            } else if (dateFrom == null && dateTo != null) {
                predicates.add(cb.lessThanOrEqualTo(t.get("trainingDate"), dateTo));
            } else if (dateFrom != null) {
                predicates.add(cb.between(t.get("trainingDate"), dateFrom, dateTo));
            }

            // Condition for training type
            if (trainingType != null) {
                predicates.add(cb.equal(t.get("trainingType"), trainingType));
            }

            // Condition for trainee ID
            predicates.add(cb.equal(t.get("trainee"), trainee));

            // Combine all predicates using AND
            cq.where(cb.and(predicates.toArray(new Predicate[0])));

//            cq.select(t).where(cb.equal(t.get("trainee"), trainee), cb.between(t.get("trainingDate"), dateFrom, dateTo), cb.equal(t.get("trainingType"), trainingType));
            List<Optional<Training>> trainings = session.createQuery(cq).getResultList()
                    .stream()
                    .map(Optional::ofNullable)
                    .toList();

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
            List<Optional<Training>> trainings = session.createQuery(cq)
                    .getResultList()
                    .stream()
                    .map(Optional::ofNullable)
                    .toList();

            log.info("DAO: Trainings found by trainer and criteria: trainer: {}, dateFrom: {}, dateTo: {}, trainingType: {}. Trainings: {}", trainer, dateFrom, dateTo, trainingType, trainings);
            return trainings;

        } catch (HibernateException e) {
            log.warn("DAO: Error to find Training by trainee and criteria: trainer: {}, dateFrom: {}, dateTo: {}, trainingType: {}. Error: {}", trainer, dateFrom, dateTo, trainingType, e.getMessage());
            throw new RuntimeException("Error to find Training by trainee and criteria: trainer: " + trainer + ", dateFrom: " + dateFrom + ", dateTo: " + dateTo + ", trainingType: " + trainingType + ". ", e);
        }
    }


    public List<Optional<Trainer>> findTrainersByTraineeFromTrainings(Trainee trainee) {
        try (Session session = sessionFactory.openSession()) {
            List<Optional<Trainer>> trainers = session.createQuery(FIND_TRAINERS_BY_TRAINEE_QUERY, Trainer.class).
                    setParameter(1, trainee)
                    .getResultList()
                    .stream()
                    .map(Optional::ofNullable)
                    .toList();
            log.info("DAO: Trainers found by trainee: {}. Trainers: {}", trainee, trainers);
            return trainers;
        } catch (HibernateException e) {
            log.warn("DAO: Error finding all trainee's trainers. Error: {}", e.getMessage());
            throw new RuntimeException("Error finding all trainee's trainers", e);
        }
    }

    public boolean existsTraineeAndTrainerInAnotherTraining(Training training, Trainee trainee, Trainer trainer) {
        try (Session session = sessionFactory.openSession()) {
            List<Optional<Training>> trainingsFiltered = session.createQuery(FIND_ANOTHER_TRAINING_BY_TRAINER_AND_TRAINEE_QUERY, Training.class)
                    .setParameter(1, trainer)
                    .setParameter(2, trainee)
                    .setParameter(3, training)
                    .getResultList()
                    .stream()
                    .map(Optional::ofNullable)
                    .toList();

            return !trainingsFiltered.isEmpty();
        } catch (HibernateException e) {
            log.warn("DAO: Error to find Training by trainer: {}. Error: {}", trainer, e.getMessage());
            throw new RuntimeException("Error to find Training by trainer: " + trainer + ". ", e);
        }
    }

    public boolean existsTraineeAndTrainerInTraining(Trainee trainee, Trainer trainer) {
        try (Session session = sessionFactory.openSession()) {
            List<Optional<Training>> trainingsFiltered = session.createQuery(FIND_TRAININGS_BY_TRAINER_AND_TRAINEE_QUERY, Training.class)
                    .setParameter(1, trainer)
                    .setParameter(2, trainee)
                    .getResultList()
                    .stream()
                    .map(Optional::ofNullable)
                    .toList();

            return !trainingsFiltered.isEmpty();
        } catch (HibernateException e) {
            log.warn("DAO: Error to find Training by trainer: {}. Error: {}", trainer, e.getMessage());
            throw new RuntimeException("Error to find Training by trainer: " + trainer + ". ", e);
        }
    }
}