package it.polito.ai.es2.services.exceptions;

public class TeamServiceException extends RuntimeException {

    public TeamServiceException() {
    }

    public TeamServiceException(String s) {
        super(s);
    }

    public TeamServiceException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public TeamServiceException(Throwable throwable) {
        super(throwable);
    }

    public TeamServiceException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
