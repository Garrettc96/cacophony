package com.example.cacophony.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Resource already exists")
public class DuplicateEntityException extends RuntimeException {
    private Class entity;

    public DuplicateEntityException(String message, Class entity) {
        super(message);
        this.entity = entity;
    }

    public DuplicateEntityException(String message, Throwable cause, Class entity) {
        super(message, cause);
        this.entity = entity;
    }

    public DuplicateEntityException(Throwable cause, Class entity) {
        super(cause);
        this.entity = entity;
    }

    public String getEntityName() {
        return this.entity.getName();
    }
}
