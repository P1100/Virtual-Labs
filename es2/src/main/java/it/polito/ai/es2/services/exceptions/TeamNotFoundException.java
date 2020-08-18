package it.polito.ai.es2.services.exceptions;

public class TeamNotFoundException extends RuntimeException {
  public TeamNotFoundException(String s) {
    super(s);
  }
}
