package org.example.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public abstract class AbstractService<T> {
    public abstract List<T> getAll();
    public abstract Optional<T> getById(long id);
    public abstract void add(T entity) throws IOException;
    public abstract void update(T entity) throws IOException;
    public abstract void deleteById(long id) throws IOException;
}