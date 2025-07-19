package org.example.exceptions;

public class NotFoundException extends BusinessException {

    public NotFoundException(String entityName, long id) {
        super(entityName + " with ID " + id + " not found.");
    }

    public NotFoundException(String message) {
        super(message);
    }
}
