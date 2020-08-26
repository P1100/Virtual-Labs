package it.polito.ai.es2.services.exceptions;

public class ImageNotFoundException extends RuntimeException {
  public ImageNotFoundException() {
    super();
  }
  
  public ImageNotFoundException(String s) {
    super(s);
  }
}
