package it.polito.ai.es2.services.exceptions;

public class StudentNotEnrolledException extends NullParameterException {
  public StudentNotEnrolledException() {
  }
  
  public StudentNotEnrolledException(String s) {
    super(s);
  }
}
