package it.polito.ai.es2.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class StudentAlreadyInTeamException extends RuntimeException {
  public StudentAlreadyInTeamException() {
    super("Student already in team.");
  }

  public StudentAlreadyInTeamException(String s) {
    super("Student " + s + " already in team. ");
  }

  public StudentAlreadyInTeamException(Long s) {
    super("Student " + s + " already in team. ");
  }
}
