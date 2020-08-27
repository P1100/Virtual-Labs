package it.polito.ai.es2.services.exceptions;

public class NullParameterException extends RuntimeException {
  public NullParameterException() {
    super("Null parameters.");
  }
  
  public NullParameterException(String s) {
    super("Null parameters. (" + s + ")");
  }
}
