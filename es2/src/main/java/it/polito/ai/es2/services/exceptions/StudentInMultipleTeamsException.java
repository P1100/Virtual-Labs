package it.polito.ai.es2.services.exceptions;

//@NoArgsConstructor
//@AllArgsConstructor
public class StudentInMultipleTeamsException extends TeamServiceException {
  public StudentInMultipleTeamsException() {
  }
  
  public StudentInMultipleTeamsException(String s) {
    super(s);
  }
  
  public StudentInMultipleTeamsException(String s, Throwable throwable) {
    super(s, throwable);
  }
  
  public StudentInMultipleTeamsException(Throwable throwable) {
    super(throwable);
  }
  
  public StudentInMultipleTeamsException(String s, Throwable throwable, boolean b, boolean b1) {
    super(s, throwable, b, b1);
  }
}
