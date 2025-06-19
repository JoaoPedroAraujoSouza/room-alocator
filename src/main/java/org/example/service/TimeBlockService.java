package org.example.service;

import org.example.models.TimeBlock;
import org.example.repository.TimeBlockRepository;
import org.example.service.interfaces.TimeBlockServiceInterface;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class TimeBlockService extends AbstractService<TimeBlock> implements TimeBlockServiceInterface {
    private final TimeBlockRepository repository = new TimeBlockRepository();

    @Override
    public List<TimeBlock> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<TimeBlock> getById(long id) {
        return repository.findById(id);
    }

    @Override
    public void add(TimeBlock timeBlock) throws IOException {
        repository.create(timeBlock);
    }

    @Override
    public void update(TimeBlock timeBlock) throws IOException {
        repository.update(timeBlock);
    }

    @Override
    public void deleteById(long id) throws IOException {
        repository.deleteById(id);
    }

    @Override
    public List<TimeBlock> getAllTimeBlocks() {
        return getAll();
    }

    @Override
    public Optional<TimeBlock> getTimeBlockById(long id) {
        return getById(id);
    }

    @Override
    public void addTimeBlock(TimeBlock timeBlock) throws IOException {
        add(timeBlock);
    }

    @Override
    public void updateTimeBlock(TimeBlock timeBlock) throws IOException {
        update(timeBlock);
    }

    @Override
    public void deleteTimeBlockById(long id) throws IOException {
        deleteById(id);
    }
}