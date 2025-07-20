package org.example.controller.api;

import java.util.List;
import java.util.Optional;

import org.example.models.Teacher;
import org.example.service.TeacherService;

public class TeacherController extends AbstractController<Teacher> {
    private final TeacherService service = new TeacherService();

    @Override
    public List<Teacher> getAll() {
        return service.getAll();
    }

    @Override
    public Optional<Teacher> getById(long id) {
        return service.getById(id);
    }

    @Override
    public boolean create(Teacher teacher) {
        try {
            service.addTeacher(teacher);
            return true;
        } catch (Exception e) {
            System.err.println("Error creating teacher: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Teacher teacher)    {
        try {
            service.updateTeacher(teacher);
            return true;
        } catch (Exception e) {
            System.err.println("Error updating teacher: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteById(long id) {
        try {
            service.deleteTeacherById(id);
            return true;
        } catch (Exception e) {
            System.err.println("Error deleting teacher: " + e.getMessage());
            return false;
        }
    }
}
