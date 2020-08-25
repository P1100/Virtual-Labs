package it.polito.ai.es2.services.exceptions;

public class TeamNotFoundException extends RuntimeException {
  public TeamNotFoundException() {
  }
  
  public TeamNotFoundException(String teamId) {
    super("Team not found! (" + teamId + ")");
  }
}
