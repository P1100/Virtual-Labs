package it.polito.ai.es2.services.exceptions;

public class ImageException extends RuntimeException {
  public ImageException() {
    super("Image error.");
  }

  public ImageException(String s) {
    super("Image error. (" + s + ")");
  }
}
