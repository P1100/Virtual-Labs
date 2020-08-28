package it.polito.ai.es2.services.exceptions;

public class CourseCardinalityConstrainsException extends RuntimeException {
  public CourseCardinalityConstrainsException() {
    super("Course cardinalities violated.");
  }

  public CourseCardinalityConstrainsException(String s) {
    super("Course cardinalities violated. (" + s + ")");
  }
}
