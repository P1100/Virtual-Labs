package it.polito.ai.es2.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class TeamAlreayCreatedException extends RuntimeException {
  public TeamAlreayCreatedException() {
    super("Team already created.");
  }

  public TeamAlreayCreatedException(String s, String c) {
    super("Team " + s + " already created in course " + c + "".trim());
  }
}
