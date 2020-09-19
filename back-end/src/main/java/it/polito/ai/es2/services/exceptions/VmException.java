package it.polito.ai.es2.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class VmException extends RuntimeException {
  public VmException(String s) {
    super("VM error: " + s);
  }
}
