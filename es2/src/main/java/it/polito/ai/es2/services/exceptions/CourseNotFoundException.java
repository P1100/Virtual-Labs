package it.polito.ai.es2.services.exceptions;

public class CourseNotFoundException extends TeamServiceException {
  public CourseNotFoundException() {
  }
  
  public CourseNotFoundException(String s) {
    super(s);
  }
}
