package org.example.service;

import org.example.models.UnavailabityPeriod;
import org.example.repository.UnavailabityPeriodRepository;
import org.example.service.interfaces.UnavailabityPeriodServiceInterface;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class UnavailabityPeriodService extends AbstractService<UnavailabityPeriod> implements UnavailabityPeriodServiceInterface {
    private final UnavailabityPeriodRepository repository = new UnavailabityPeriodRepository();

    @Override
    public List<UnavailabityPeriod> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<UnavailabityPeriod> getById(long id) {
        return repository.findById(id);
    }

    @Override
    public void add(UnavailabityPeriod period) throws IOException {
        repository.create(period);
    }

    @Override
    public void update(UnavailabityPeriod period) throws IOException {
        repository.update(period);
    }

    @Override
    public void deleteById(long id) throws IOException {
        repository.deleteById(id);
    }

    @Override
    public List<UnavailabityPeriod> getAllPeriods() {
        return getAll();
    }

    @Override
    public Optional<UnavailabityPeriod> getPeriodById(long id) {
        return getById(id);
    }

    @Override
    public void addPeriod(UnavailabityPeriod period) throws IOException {
        add(period);
    }

    @Override
    public void updatePeriod(UnavailabityPeriod period) throws IOException {
        update(period);
    }

    @Override
    public void deletePeriodById(long id) throws IOException {
        deleteById(id);
    }
}