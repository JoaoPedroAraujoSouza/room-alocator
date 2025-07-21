package org.example.repository.interfaces;

import org.example.models.Subject;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface SubjectRepositoryInterface {
    List<Subject> findAll();
    Optional<Subject> findById(long id);
    void create(Subject subject) throws IOException;
    void update(Subject subject) throws IOException;
    void deleteById(long id) throws IOException;
}