package it.polito.ai.es2.services.exceptions;

public class CourseNotFoundException extends TeamServiceException {
  public CourseNotFoundException() {
  }
  
  public CourseNotFoundException(String s) {
    super(s);
  }
  
  public CourseNotFoundException(String s, Throwable throwable) {
    super(s, throwable);
  }
  
  public CourseNotFoundException(Throwable throwable) {
    super(throwable);
  }
  
  public CourseNotFoundException(String s, Throwable throwable, boolean b, boolean b1) {
    super(s, throwable, b, b1);
  }
}
