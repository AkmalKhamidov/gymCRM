package com.epamlearning.daos;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface EntityDAO<T> {

    Optional<T> saveOrUpdate(T t);
//    Optional<T> update(Long id, T t);
    Optional<T> findById(Long id);
    List<Optional<T>> findAll();
    void delete(T t);

    Optional<T> findByUserName(String username);

}
