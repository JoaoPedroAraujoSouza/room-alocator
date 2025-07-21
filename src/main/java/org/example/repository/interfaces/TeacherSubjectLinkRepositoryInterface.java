package org.example.repository.interfaces;

import org.example.models.TeacherSubjectLink;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface TeacherSubjectLinkRepositoryInterface {
    List<TeacherSubjectLink> findAll();
    Optional<TeacherSubjectLink> findById(long id);
    void create(TeacherSubjectLink link) throws IOException;
    void update(TeacherSubjectLink link) throws IOException;
    void deleteById(long id) throws IOException;
}