package it.polito.ai.es2.services.exceptions;

public class VlException extends RuntimeException {
  public VlException() {
    super("Generic Error.");
  }

  public VlException(String s) {
    super("Generic Error. (" + s + ")");
  }
}
