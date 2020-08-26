package it.polito.ai.es2.services.exceptions;

public class VlException extends RuntimeException {
  public VlException() {
    super();
  }
  
  public VlException(String s) {
    super(s);
  }
}
