package org.example.service;

import org.example.models.Subject;
import org.example.repository.SubjectRepository;
import org.example.service.interfaces.SubjectServiceInterface;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class SubjectService extends AbstractService<Subject> implements SubjectServiceInterface {
    private final SubjectRepository repository = new SubjectRepository();

    @Override
    public List<Subject> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Subject> getById(long id) {
        return repository.findById(id);
    }

    @Override
    public void add(Subject subject) throws IOException {
        repository.create(subject);
    }

    @Override
    public void update(Subject subject) throws IOException {
        repository.update(subject);
    }

    @Override
    public void deleteById(long id) throws IOException {
        repository.deleteById(id);
    }

    @Override
    public List<Subject> getAllSubjects() {
        return getAll();
    }

    @Override
    public Optional<Subject> getSubjectById(long id) {
        return getById(id);
    }

    @Override
    public void addSubject(Subject subject) throws IOException {
        add(subject);
    }

    @Override
    public void updateSubject(Subject subject) throws IOException {
        update(subject);
    }

    @Override
    public void deleteSubjectById(long id) throws IOException {
        deleteById(id);
    }
}