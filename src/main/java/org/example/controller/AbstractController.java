package org.example.controller;

import java.util.List;
import java.util.Optional;

public abstract class AbstractController<T> {
    public abstract List<T> getAll();
    public abstract Optional<T> getById(long id);
    public abstract boolean create(T entity);
    public abstract boolean update(T entity);
    public abstract boolean deleteById(long id);
}
