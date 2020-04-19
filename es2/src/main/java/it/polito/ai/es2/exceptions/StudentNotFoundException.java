package it.polito.ai.es2.exceptions;

public class StudentNotFoundException extends TeamServiceException {
    public StudentNotFoundException() {
    }

    public StudentNotFoundException(String s) {
        super(s);
    }

    public StudentNotFoundException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public StudentNotFoundException(Throwable throwable) {
        super(throwable);
    }

    public StudentNotFoundException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
