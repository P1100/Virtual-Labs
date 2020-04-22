package it.polito.ai.es2.services.exceptions;

public class StudentNotEnrolledException extends TeamServiceException {
  public StudentNotEnrolledException() {
  }
  
  public StudentNotEnrolledException(String s) {
    super(s);
  }
  
  public StudentNotEnrolledException(String s, Throwable throwable) {
    super(s, throwable);
  }
  
  public StudentNotEnrolledException(Throwable throwable) {
    super(throwable);
  }
  
  public StudentNotEnrolledException(String s, Throwable throwable, boolean b, boolean b1) {
    super(s, throwable, b, b1);
  }
}
