package it.polito.ai.es2.services.exceptions;

public class StudentNotFoundException extends NullParameterException {
  public StudentNotFoundException() {
  }
  
  public StudentNotFoundException(String studentId) {
    super("Student not found! (" + studentId + ")");
  }
}
