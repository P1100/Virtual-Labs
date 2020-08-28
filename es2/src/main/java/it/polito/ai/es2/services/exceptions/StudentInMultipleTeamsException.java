package it.polito.ai.es2.services.exceptions;

//@NoArgsConstructor
//@AllArgsConstructor
public class StudentInMultipleTeamsException extends RuntimeException {
  public StudentInMultipleTeamsException() {
    super("Student in multiple teams.");
  }

  public StudentInMultipleTeamsException(String s) {
    super("Student " + s + " in multiple teams. ");
  }
}
