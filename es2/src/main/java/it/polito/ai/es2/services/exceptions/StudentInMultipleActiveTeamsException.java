package it.polito.ai.es2.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class StudentInMultipleActiveTeamsException extends RuntimeException {
  public StudentInMultipleActiveTeamsException() {
    super("Student in multiple teams.");
  }

  public StudentInMultipleActiveTeamsException(String s) {
    super("Student " + s + " in multiple teams. ");
  }
  public StudentInMultipleActiveTeamsException(Long s) {
    super("Student " + s + " in multiple teams. ");
  }
}
