package it.polito.ai.es2.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ImageNotFoundException extends RuntimeException {
  public ImageNotFoundException() {
    super("Image not found.");
  }

  public ImageNotFoundException(String s) {
    super("Image " + s + " not found. ");
  }
}
