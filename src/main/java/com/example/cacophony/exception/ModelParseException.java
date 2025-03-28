package com.example.cacophony.exception;

public class ModelParseException extends RuntimeException {
    public ModelParseException(String err) {
        super(err);
    }

    public ModelParseException(String err, Throwable ex) {
        super(err, ex);
    }
}
