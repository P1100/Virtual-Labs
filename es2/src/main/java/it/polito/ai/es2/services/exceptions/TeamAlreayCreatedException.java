package it.polito.ai.es2.services.exceptions;

public class TeamAlreayCreatedException extends RuntimeException {
  public TeamAlreayCreatedException() {
    super();
  }
  
  public TeamAlreayCreatedException(String message) {
    super(message);
  }
  
  public TeamAlreayCreatedException(String message, Throwable cause) {
    super(message, cause);
  }
  
  public TeamAlreayCreatedException(Throwable cause) {
    super(cause);
  }
  
  protected TeamAlreayCreatedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
