package org.example.service;

import org.example.models.Teacher;
import org.example.repository.TeacherRepository;
import org.example.service.interfaces.TeacherServiceInterface;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class TeacherService extends AbstractService<Teacher> implements TeacherServiceInterface {
    private final TeacherRepository repository = new TeacherRepository();

    @Override
    public List<Teacher> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Teacher> getById(long id) {
        return repository.findById(id);
    }

    @Override
    public void add(Teacher teacher) throws IOException {
        repository.create(teacher);
    }

    @Override
    public void update(Teacher teacher) throws IOException {
        repository.update(teacher);
    }

    @Override
    public void deleteById(long id) throws IOException {
        repository.deleteById(id);
    }

    @Override
    public List<Teacher> getAllTeachers() {
        return getAll();
    }

    @Override
    public Optional<Teacher> getTeacherById(long id) {
        return getById(id);
    }

    @Override
    public void addTeacher(Teacher teacher) throws IOException {
        add(teacher);
    }

    @Override
    public void updateTeacher(Teacher teacher) throws IOException {
        update(teacher);
    }

    @Override
    public void deleteTeacherById(long id) throws IOException {
        deleteById(id);
    }
}