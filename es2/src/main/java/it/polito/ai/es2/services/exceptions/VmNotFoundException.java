package it.polito.ai.es2.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class VmNotFoundException extends RuntimeException {
  public VmNotFoundException(Long s) {
    super("VM not found: " + s);
  }

  public VmNotFoundException(String s) {
    super("VM not found: " + s);
  }
}
