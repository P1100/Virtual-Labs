package it.polito.ai.es2.services.exceptions;

public class TeamAlreayCreatedException extends RuntimeException {
  public TeamAlreayCreatedException() {
    super("Team already created.");
  }

  public TeamAlreayCreatedException(String s) {
    super("Team already created. (" + s + ")");
  }
}
