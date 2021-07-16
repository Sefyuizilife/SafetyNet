package com.safetynet.safetynetalerts.repositories;

import java.util.List;

public interface IRepository<T> {

    List<T> findAll();

    T save(T entity);

    void delete(T entity);

}
