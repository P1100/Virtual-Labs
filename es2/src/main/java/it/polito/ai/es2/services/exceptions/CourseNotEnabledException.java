package it.polito.ai.es2.services.exceptions;

public class CourseNotEnabledException extends TeamServiceException {
  public CourseNotEnabledException() {
  }
  
  public CourseNotEnabledException(String s) {
    super(s);
  }
}
