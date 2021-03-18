package it.polito.ai.es2.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class CourseNotEnabledException extends RuntimeException {
  public CourseNotEnabledException() {
    super("Course not enabled.");
  }

  public CourseNotEnabledException(String s) {
    super("Course " + s + " not enabled. ");
  }
}
