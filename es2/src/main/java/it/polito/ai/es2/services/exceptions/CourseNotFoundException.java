package it.polito.ai.es2.services.exceptions;

public class CourseNotFoundException extends RuntimeException {
  public CourseNotFoundException() {
    super("Course not found.");
  }

  public CourseNotFoundException(String courseId) {
    super("Course " + courseId + " not found. ");
  }
}
