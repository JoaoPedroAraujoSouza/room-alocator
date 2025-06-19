package org.example.service;

import org.example.models.TimeAllocation;
import org.example.repository.TimeAllocationRepository;
import org.example.service.interfaces.TimeAllocationServiceInterface;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class TimeAllocationService extends AbstractService<TimeAllocation> implements TimeAllocationServiceInterface {
    private final TimeAllocationRepository repository = new TimeAllocationRepository();

    @Override
    public List<TimeAllocation> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<TimeAllocation> getById(long id) {
        return repository.findById(id);
    }

    @Override
    public void add(TimeAllocation allocation) throws IOException {
        repository.create(allocation);
    }

    @Override
    public void update(TimeAllocation allocation) throws IOException {
        repository.update(allocation);
    }

    @Override
    public void deleteById(long id) throws IOException {
        repository.deleteById(id);
    }

    @Override
    public List<TimeAllocation> getAllAllocations() {
        return getAll();
    }

    @Override
    public Optional<TimeAllocation> getAllocationById(long id) {
        return getById(id);
    }

    @Override
    public void addAllocation(TimeAllocation allocation) throws IOException {
        add(allocation);
    }

    @Override
    public void updateAllocation(TimeAllocation allocation) throws IOException {
        update(allocation);
    }

    @Override
    public void deleteAllocationById(long id) throws IOException {
        deleteById(id);
    }
}