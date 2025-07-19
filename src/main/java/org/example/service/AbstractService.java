package org.example.service;

import org.example.exceptions.ConflictException;
import org.example.exceptions.NotFoundException;
import org.example.exceptions.ValidationException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public abstract class AbstractService<T> {
    public abstract List<T> getAll();
    public abstract Optional<T> getById(long id);
    public abstract void add(T entity) throws ValidationException, ConflictException, IOException;
    public abstract void update(T entity) throws ConflictException, ValidationException, NotFoundException, IOException;
    public abstract void deleteById(long id) throws NotFoundException, IOException;
}