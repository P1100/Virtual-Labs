package it.polito.ai.es2.services.exceptions;

public class StudentNotFoundException extends RuntimeException {
  public StudentNotFoundException() {
    super("Student not found.");
  }

  public StudentNotFoundException(String s) {
    super("Student " + s + " not found. ");
  }
}
