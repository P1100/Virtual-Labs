package it.polito.ai.es2.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class StudentAlreadyEnrolledException extends RuntimeException {
  public StudentAlreadyEnrolledException() {
    super("Student already enrolled!");
  }

  public StudentAlreadyEnrolledException(String s, String c) {
    super("Student " + s + " already enrolled in course " + c);
  }
}
