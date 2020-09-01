package it.polito.ai.es2.services.exceptions;

public class UserAlreadyEnabled extends RuntimeException {
  public UserAlreadyEnabled(String s) {
    super("User " + s + " already enabled");
  }
}
