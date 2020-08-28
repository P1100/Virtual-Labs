package it.polito.ai.es2.services.exceptions;

public class TeamNotFoundException extends RuntimeException {
  public TeamNotFoundException() {
    super("Team not found.");
  }

  public TeamNotFoundException(String teamId) {
    super("Team not found. (" + teamId + ")");
  }
}
