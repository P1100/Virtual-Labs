package it.polito.ai.es2.services.exceptions;

public class ImageNotFoundException extends RuntimeException {
  public ImageNotFoundException() {
    super("Image not found.");
  }

  public ImageNotFoundException(String s) {
    super("Image " + s + " not found. ");
  }
}
