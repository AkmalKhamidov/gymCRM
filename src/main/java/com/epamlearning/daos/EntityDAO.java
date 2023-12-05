package com.epamlearning.daos;

import com.epamlearning.models.Trainer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface EntityDAO<T> {

    Optional<T> save(T t);
    Optional<T> update(Long id, T t);
    Optional<T> findById(Long id);
    List<Optional<T>> findAll();
    void deleteById(Long id);

//    Optional<T> findByProperty(String property, Object o);

    Optional<T> findByUserName(String username);

}
