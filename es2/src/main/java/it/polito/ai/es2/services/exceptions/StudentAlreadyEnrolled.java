package it.polito.ai.es2.services.exceptions;

import it.polito.ai.es2.services.exceptions.newe.VLServiceException;

public class StudentAlreadyEnrolled extends RuntimeException {
    public StudentAlreadyEnrolled() {
        super("Student already enrolled!");
    }
    
    public StudentAlreadyEnrolled(String s) {
        super("Student already enrolled! (" + s + ")");
    }
}
