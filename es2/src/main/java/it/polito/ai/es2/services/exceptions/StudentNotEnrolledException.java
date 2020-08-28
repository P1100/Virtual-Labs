package it.polito.ai.es2.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class StudentNotEnrolledException extends RuntimeException {
  public StudentNotEnrolledException() {
    super("Student not enrolled.");
  }

  public StudentNotEnrolledException(String s) {
    super("Student " + s + " not enrolled. ");
  }
}
