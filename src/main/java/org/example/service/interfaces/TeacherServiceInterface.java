package org.example.service.interfaces;

import org.example.exceptions.NotFoundException;
import org.example.models.Teacher;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.example.exceptions.ValidationException;

public interface TeacherServiceInterface {
    List<Teacher> getAllTeachers();
    Optional<Teacher> getTeacherById(long id);
    void addTeacher(Teacher teacher)  throws  ValidationException, IOException;
    void updateTeacher(Teacher teacher) throws ValidationException, NotFoundException, IOException;
    void deleteTeacherById(long id) throws NotFoundException, IOException;
}