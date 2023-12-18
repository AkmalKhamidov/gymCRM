package com.epamlearning.daos;

import com.epamlearning.models.User;
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
public class UserDAOImpl implements EntityDAO<User> {

    private final SessionFactory sessionFactory;

    @Value("${FIND_ALL_USERS_QUERY}")
    private String FIND_ALL_USERS_QUERY;
    @Value("${FIND_USER_BY_USERNAME_QUERY}")
    private String FIND_USER_BY_USERNAME_QUERY;

    @Autowired
    public UserDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<User> saveOrUpdate(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(user);
            session.getTransaction().commit();
            log.info("DAO: User inserted/updated. User: {}", user);
            return Optional.of(user);
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.find(User.class, id);
            log.info("DAO: User found. User: {}", user);
            return Optional.of(user);
        } catch (HibernateException e) {
            log.warn("DAO: Error finding User with ID: {}. Error: {}", id, e.getMessage());
            throw new RuntimeException("Error finding User with ID " + id, e);
        }
    }

    @Override
    public List<Optional<User>> findAll() {
        try (Session session = sessionFactory.openSession()) {
            List<Optional<User>> users = session.createQuery(FIND_ALL_USERS_QUERY, User.class)
                    .getResultList()
                    .stream()
                    .map(Optional::ofNullable)
                    .toList();
            log.info("DAO: Users found. Users: {}", users);
            return users;
        } catch (HibernateException e) {
            log.warn("DAO: Error finding all trainees. Error: {}", e.getMessage());
            throw new RuntimeException("Error finding all Users", e);
        }
    }

    @Override
    public void delete(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(user);
            session.getTransaction().commit();
            log.info("DAO: User deleted. User: {}", user);
        } catch (HibernateException e) {
            log.warn("DAO: Error delete User: {}. Error: {}", user, e.getMessage());
            throw new RuntimeException("Error delete User: " + user, e);
        }
    }

    @Override
    public Optional<User> findByUserName(String username) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.createQuery(FIND_USER_BY_USERNAME_QUERY, User.class)
                    .setParameter(1, username)
                    .getSingleResultOrNull();
            log.info("DAO: User found by username: {}. User: {}", username, user);
            return Optional.ofNullable(user);
        } catch (HibernateException e) {
            log.warn("DAO: Error to find by username: {}. Error: {}", username, e.getMessage());
            throw new RuntimeException("Error to find by username: " + username, e);
        }
    }
}
