package org.example.repository;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractRepository<T extends Serializable> {
    private final GenericRepository<T> fileRepository;
    private List<T> items;

    protected AbstractRepository(String fileName) {
        this.fileRepository = new GenericRepository<>(fileName);
        try {
            this.items = fileRepository.loadAll();
        } catch (Exception e) {
            this.items = new ArrayList<>();
        }
    }

    public List<T> findAll() {
        return new ArrayList<>(items);
    }

    public Optional<T> findById(long id) {
        return items.stream()
                .filter(item -> getId(item) == id)
                .findFirst();
    }

    public void create(T item) throws IOException {
        items.add(item);
        fileRepository.saveAll(items);
    }

    public void update(T item) throws IOException {
        deleteById(getId(item));
        items.add(item);
        fileRepository.saveAll(items);
    }

    public void deleteById(long id) throws IOException {
        items.removeIf(item -> getId(item) == id);
        fileRepository.saveAll(items);
    }

    protected abstract long getId(T item);
}