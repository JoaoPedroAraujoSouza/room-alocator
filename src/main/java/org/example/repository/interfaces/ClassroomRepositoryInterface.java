package org.example.repository.interfaces;

import org.example.models.Classroom;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ClassroomRepositoryInterface {
    List<Classroom> findAll();
    Optional<Classroom> findById(long id);
    void create(Classroom classroom) throws IOException;
    void update(Classroom classroom) throws IOException;
    void deleteById(long id) throws IOException;
}