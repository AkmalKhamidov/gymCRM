package com.epamlearning.services;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface BaseService<T> {

        Optional<T> save(T t);
        Optional<T> update(Long id, T t);
        Optional<T> findById(Long id);
        List<Optional<T>> findAll();
        void deleteById(Long id);

        Optional<T> findByUsername(String username);
}
