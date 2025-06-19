package org.example.service;

import org.example.models.Classroom;
import org.example.repository.ClassroomRepository;
import org.example.service.interfaces.ClassroomServiceInterface;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ClassroomService extends AbstractService<Classroom> implements ClassroomServiceInterface {
    private final ClassroomRepository repository = new ClassroomRepository();

    @Override
    public List<Classroom> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Classroom> getById(long id) {
        return repository.findById(id);
    }

    @Override
    public void add(Classroom classroom) throws IOException {
        repository.create(classroom);
    }

    @Override
    public void update(Classroom classroom) throws IOException {
        repository.update(classroom);
    }

    @Override
    public void deleteById(long id) throws IOException {
        repository.deleteById(id);
    }

    @Override
    public List<Classroom> getAllClassrooms() {
        return getAll();
    }

    @Override
    public Optional<Classroom> getClassroomById(long id) {
        return getById(id);
    }

    @Override
    public void addClassroom(Classroom classroom) throws IOException {
        add(classroom);
    }

    @Override
    public void updateClassroom(Classroom classroom) throws IOException {
        update(classroom);
    }

    @Override
    public void deleteClassroomById(long id) throws IOException {
        deleteById(id);
    }
}