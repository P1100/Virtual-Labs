package it.polito.ai.es2.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class CourseNotFoundException extends RuntimeException {
  public CourseNotFoundException() {
    super("Course not found."); // used for null values
  }

  public CourseNotFoundException(String courseId) {
    super("Course " + courseId + " not found. ");
  }
}
