package it.polito.ai.es2.services.exceptions;

public class CourseNotEnabledException extends TeamServiceException {
  public CourseNotEnabledException() {
  }
  
  public CourseNotEnabledException(String s) {
    super(s);
  }
  
  public CourseNotEnabledException(String s, Throwable throwable) {
    super(s, throwable);
  }
  
  public CourseNotEnabledException(Throwable throwable) {
    super(throwable);
  }
  
  public CourseNotEnabledException(String s, Throwable throwable, boolean b, boolean b1) {
    super(s, throwable, b, b1);
  }
}
