package it.polito.ai.es2.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class CourseCardinalityConstrainsException extends RuntimeException {
  public CourseCardinalityConstrainsException() {
    super("Course cardinalities violated.");
  }

  public CourseCardinalityConstrainsException(String s, String t) {
    super("Course " + s + " cardinalities violated. " + t);
  }
}
