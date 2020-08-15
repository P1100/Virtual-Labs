package it.polito.ai.es2.services.exceptions;

public class TeamAlreayCreatedException extends RuntimeException {
  public TeamAlreayCreatedException() {
    super();
  }
  
  public TeamAlreayCreatedException(String message) {
    super(message);
  }
}
