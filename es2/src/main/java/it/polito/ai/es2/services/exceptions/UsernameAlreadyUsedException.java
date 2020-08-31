package it.polito.ai.es2.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class UsernameAlreadyUsedException extends RuntimeException {
  public UsernameAlreadyUsedException(String s) {
    super("Username " + s + " already used. ");
  }
}
