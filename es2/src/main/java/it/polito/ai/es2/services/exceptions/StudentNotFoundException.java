package it.polito.ai.es2.services.exceptions;

public class StudentNotFoundException extends RuntimeException {
  public StudentNotFoundException() {
    super("Student not found.");
  }
  
  public StudentNotFoundException(String studentId) {
    super("Student not found. (" + studentId + ")");
  }
}
