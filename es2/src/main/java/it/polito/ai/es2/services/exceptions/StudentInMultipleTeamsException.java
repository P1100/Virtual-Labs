package it.polito.ai.es2.services.exceptions;

//@NoArgsConstructor
//@AllArgsConstructor
public class StudentInMultipleTeamsException extends TeamServiceException {
  public StudentInMultipleTeamsException() {
  }
  
  public StudentInMultipleTeamsException(String s) {
    super(s);
  }
}
