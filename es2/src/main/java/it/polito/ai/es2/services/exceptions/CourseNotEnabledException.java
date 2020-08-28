package it.polito.ai.es2.services.exceptions;

public class CourseNotEnabledException extends RuntimeException {
  public CourseNotEnabledException() {
    super("Course not enabled.");
  }

  public CourseNotEnabledException(String s) {
    super("Course " + s + " not enabled. ");
  }
}
