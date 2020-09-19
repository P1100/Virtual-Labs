package it.polito.ai.es2.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class FailedUpdateException extends RuntimeException {
  public FailedUpdateException(String s) {
    super(s);
  }
}
