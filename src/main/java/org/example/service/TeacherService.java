package org.example.service;

import org.example.exceptions.NotFoundException;
import org.example.exceptions.ValidationException;
import org.example.models.Teacher;
import org.example.repository.TeacherRepository;
import org.example.service.interfaces.TeacherServiceInterface;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class TeacherService extends AbstractService<Teacher> implements TeacherServiceInterface {

    private final TeacherRepository repository;

    public TeacherService(TeacherRepository repository) {
        this.repository = repository;
    }

    public TeacherService() {
        this.repository = new TeacherRepository();
    }

    private void validate(Teacher teacher) throws ValidationException {
        if (teacher.getName() == null || teacher.getName().trim().isEmpty()) {
            throw new ValidationException("Teacher name cannot be empty");
        }
        String cpf = teacher.getCpf();
        if (cpf == null || !Pattern.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", cpf)) {
            throw new ValidationException("Teacher CPF invalid");
        }
        String email = teacher.getEmail();
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (email == null || !Pattern.compile(emailRegex).matcher(email).matches()) {
            throw new ValidationException("Teacher email invalid");
        }
    }

    @Override
    public List<Teacher> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Teacher> getById(long id) {
        return repository.findById(id);
    }

    @Override
    public void add(Teacher teacher) throws ValidationException, IOException {
        validate(teacher);
        repository.create(teacher);
    }

    @Override
    public void update(Teacher teacher) throws ValidationException, NotFoundException, IOException {
        validate(teacher);
        repository.findById(teacher.getId())
                .orElseThrow(() -> new NotFoundException("Teacher not found with id: " + teacher.getId()));
        repository.update(teacher);
    }

    @Override
    public void deleteById(long id) throws NotFoundException, IOException {
        repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Teacher not found with id: " + id));
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
    public void addTeacher(Teacher teacher) throws ValidationException, IOException {
        add(teacher);
    }

    @Override
    public void updateTeacher(Teacher teacher) throws ValidationException, NotFoundException, IOException {
        update(teacher);
    }

    @Override
    public void deleteTeacherById(long id) throws NotFoundException, IOException {
        deleteById(id);
    }
}