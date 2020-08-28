package it.polito.ai.es2.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class StudentInMultipleTeamsException extends RuntimeException {
  public StudentInMultipleTeamsException() {
    super("Student in multiple teams.");
  }

  public StudentInMultipleTeamsException(String s) {
    super("Student " + s + " in multiple teams. ");
  }
}
