package it.polito.ai.es2.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class ImageException extends RuntimeException {
  public ImageException() {
    super("Image error.");
  }

  public ImageException(String s) {
    super("Image error. (" + s + ")");
  }
}
