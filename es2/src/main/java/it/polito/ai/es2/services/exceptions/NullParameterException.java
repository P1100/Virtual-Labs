package it.polito.ai.es2.services.exceptions;

public class NullParameterException extends RuntimeException {
  public NullParameterException() {
  }
  
  public NullParameterException(String s) {
    super(s);
  }
}
