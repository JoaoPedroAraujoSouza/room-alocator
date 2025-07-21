package org.example.repository.interfaces;

import org.example.models.Teacher;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface TeacherRepositoryInterface {
    List<Teacher> findAll();
    Optional<Teacher> findById(long id);
    void create(Teacher teacher) throws IOException;
    void update(Teacher teacher) throws IOException;
    void deleteById(long id) throws IOException;
}