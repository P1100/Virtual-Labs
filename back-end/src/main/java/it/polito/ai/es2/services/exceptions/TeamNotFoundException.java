package it.polito.ai.es2.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class TeamNotFoundException extends RuntimeException {
  public TeamNotFoundException() {
    super("Team not found.");
  }

  public TeamNotFoundException(String team) {
    super(("Team " + team + " not found. ").trim());
  }

  public TeamNotFoundException(Long team) {
    super(("Team " + team + " not found. ").trim());
  }
}
