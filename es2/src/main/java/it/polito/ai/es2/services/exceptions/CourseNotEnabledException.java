package it.polito.ai.es2.services.exceptions;

public class CourseNotEnabledException extends NullParameterException {
  public CourseNotEnabledException() {
  }
  
  public CourseNotEnabledException(String s) {
    super("Course not enabled! (" + s + ")");
  }
}
