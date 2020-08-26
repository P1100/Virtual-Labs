package it.polito.ai.es2.services.exceptions;

public class ImageException extends RuntimeException {
  public ImageException() {
    super();
  }
  
  public ImageException(String s) {
    super(s);
  }
}
