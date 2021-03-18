package it.polito.ai.es2.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class StudentsInMultipleActiveTeamsException extends RuntimeException {
  public StudentsInMultipleActiveTeamsException() {
    super("Student in multiple teams.");
  }

  public StudentsInMultipleActiveTeamsException(String s) {
    super("Student " + s + " in multiple teams. ");
  }

  public StudentsInMultipleActiveTeamsException(Long s) {
    super("Student " + s + " in multiple teams. ");
  }
}
