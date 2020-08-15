package it.polito.ai.es2.services.exceptions;

public class TeamServiceException extends RuntimeException {
  
  public TeamServiceException() {
  }
  public TeamServiceException(String s) {
    super(s);
  }
}
