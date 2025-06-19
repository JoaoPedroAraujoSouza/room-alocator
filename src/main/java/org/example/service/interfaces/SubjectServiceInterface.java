package org.example.service.interfaces;

import org.example.models.Subject;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface SubjectServiceInterface {
    List<Subject> getAllSubjects();
    Optional<Subject> getSubjectById(long id);
    void addSubject(Subject subject) throws IOException;
    void updateSubject(Subject subject) throws IOException;
    void deleteSubjectById(long id) throws IOException;
}