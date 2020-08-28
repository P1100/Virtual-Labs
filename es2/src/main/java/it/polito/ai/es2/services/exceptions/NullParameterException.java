package it.polito.ai.es2.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class NullParameterException extends RuntimeException {
  public NullParameterException() {
    super("Null parameters.");
  }

  public NullParameterException(String s) {
    super("Null parameters. (" + s + ")");
  }
}
