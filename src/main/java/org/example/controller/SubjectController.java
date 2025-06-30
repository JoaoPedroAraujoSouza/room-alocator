package org.example.controller;

import org.example.models.Subject;
import org.example.service.SubjectService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class SubjectController extends AbstractController<Subject> {
    private final SubjectService service = new SubjectService();

    @Override
    public List<Subject> getAll() {
        return service.getAllSubjects();
    }

    @Override
    public Optional<Subject> getById(long id) {
        return service.getSubjectById(id);
    }

    @Override
    public boolean create(Subject subject) {
        try {
            service.addSubject(subject);
            return true;
        } catch (IOException e) {
            System.err.println("Erro ao criar disciplina: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Subject subject) {
        try {
            service.updateSubject(subject);
            return true;
        } catch (IOException e) {
            System.err.println("Erro ao atualizar disciplina: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteById(long id) {
        try {
            service.deleteSubjectById(id);
            return true;
        } catch (IOException e) {
            System.err.println("Erro ao deletar disciplina: " + e.getMessage());
            return false;
        }
    }
}