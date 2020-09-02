package it.polito.ai.es2.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class UserAlreadyEnabled extends RuntimeException {
  public UserAlreadyEnabled(String s) {
    super("User " + s + " already enabled");
  }
}
