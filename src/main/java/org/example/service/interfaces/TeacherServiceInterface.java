package org.example.service.interfaces;

import org.example.models.Teacher;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface TeacherServiceInterface {
    List<Teacher> getAllTeachers();
    Optional<Teacher> getTeacherById(long id);
    void addTeacher(Teacher teacher) throws IOException;
    void updateTeacher(Teacher teacher) throws IOException;
    void deleteTeacherById(long id) throws IOException;
}