package com.epamlearning.daos;

import com.epamlearning.models.User;
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
public class UserDAOImpl implements EntityDAO<User> {

    private SessionFactory sessionFactory;

    @Autowired
    public UserDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<User> save(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(user); // session.save() is deprecated
            session.getTransaction().commit();
            log.info("DAO: User inserted. User: {}", user);
            return Optional.of(user);
        }
    }

    @Override
    public Optional<User> update(Long id, User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User userToUpdate = session.find(User.class, id);
            userToUpdate.setUsername(user.getUsername());
            userToUpdate.setPassword(user.getPassword());
            userToUpdate.setFirstName(user.getFirstName());
            userToUpdate.setLastName(user.getLastName());
            userToUpdate.setActive(user.isActive());
//            session.merge(userToUpdate);
            session.getTransaction().commit();
            log.info("DAO: User updated. Id: {}, user: {}", id, userToUpdate);
            return Optional.of(userToUpdate);
        } catch (HibernateException e) {
            log.warn("DAO: Error updating User with ID: {}. Error: {}", id, e.getMessage());
            throw new RuntimeException("Error updating User with ID " + id, e);
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
            List<Optional<User>> users = session.createQuery("from User").getResultList();
            log.info("DAO: Users found. Users: {}", users);
            return users;
        } catch (HibernateException e) {
            log.warn("DAO: Error finding all trainees. Error: {}", e.getMessage());
            throw new RuntimeException("Error finding all Users", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            User userToDelete = session.find(User.class, id);
            session.remove(userToDelete);
            session.getTransaction().commit();
            log.info("DAO: User deleted. Id: {}, user: {}", id, userToDelete);
        } catch (HibernateException e) {
            log.warn("DAO: Error delete User with ID: {}. Error: {}", id, e.getMessage());
            throw new RuntimeException("Error delete User with ID " + id, e);
        }
    }

    @Override
    public Optional<User> findByUserName(String username) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.createQuery("SELECT u FROM User u WHERE u.username=:value", User.class)
                    .setParameter("value", username)
                    .getSingleResultOrNull();
            log.info("DAO: User found by username: {}. User: {}", username, user);
            return Optional.ofNullable(user);
        } catch (HibernateException e) {
            log.warn("DAO: Error to find by username: {}. Error: {}", username, e.getMessage());
            throw new RuntimeException("Error to find by username: " + username, e);
        }
    }

    public Optional<User> updatePassword(Long id, String password) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User userToUpdate = session.find(User.class, id);
            userToUpdate.setPassword(password);
            session.getTransaction().commit();
            log.info("DAO: User password updated. Id: {}, user: {}", id, userToUpdate);
            return Optional.of(userToUpdate);
        } catch (HibernateException e) {
            log.warn("DAO: Error updating User password with ID: {}. Error: {}", id, e.getMessage());
            throw new RuntimeException("Error updating User password with ID " + id, e);
        }
    }

    public Optional<User> updateActive(Long id, boolean active) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User userToUpdate = session.find(User.class, id);
            userToUpdate.setActive(active);
            session.getTransaction().commit();
            log.info("DAO: User active updated. Id: {}, user: {}", id, userToUpdate);
            return Optional.of(userToUpdate);
        } catch (HibernateException e) {
            log.warn("Error updating User active with ID: {}. Error: {}", id, e.getMessage());
            throw new RuntimeException("Error updating User active with ID " + id, e);
        }
    }
}
