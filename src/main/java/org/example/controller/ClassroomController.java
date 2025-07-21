package org.example.controller.api;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.example.models.Classroom;
import org.example.service.ClassroomService;

public class ClassroomController extends AbstractController<Classroom> {
    private final ClassroomService service = new ClassroomService();

    @Override
    public List<Classroom> getAll() {
        return service.getAll();
    }

    @Override
    public Optional<Classroom> getById(long id) {
        return service.getById(id);
    }

    @Override
    public boolean create(Classroom classroom) {
        try {
            service.add(classroom);
            return true;
        } catch (IOException e) {
            System.err.println("Erro ao criar turma: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Classroom classroom) {
        try {
            service.update(classroom);
            return true;
        } catch (IOException e) {
            System.err.println("Erro ao atualizar turma: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteById(long id) {
        try {
            service.deleteById(id);
            return true;
        } catch (IOException e) {
            System.err.println("Erro ao deletar turma: " + e.getMessage());
            return false;
        }
    }
}