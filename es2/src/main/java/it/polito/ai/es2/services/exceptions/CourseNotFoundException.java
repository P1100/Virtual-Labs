package it.polito.ai.es2.services.exceptions;

public class CourseNotFoundException extends NullParameterException {
  public CourseNotFoundException() {
  }
  
  public CourseNotFoundException(String courseId) {
    super("Course not found! (" + courseId + ")");
  }
}
