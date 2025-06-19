package org.example.service.interfaces;

import org.example.models.Classroom;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ClassroomServiceInterface {
    List<Classroom> getAllClassrooms();
    Optional<Classroom> getClassroomById(long id);
    void addClassroom(Classroom classroom) throws IOException;
    void updateClassroom(Classroom classroom) throws IOException;
    void deleteClassroomById(long id) throws IOException;
}