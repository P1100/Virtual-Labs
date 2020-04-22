package it.polito.ai.es2.services.exceptions;

public class StudentDuplicatesInProposalException extends TeamServiceException {
  public StudentDuplicatesInProposalException() {
  }
  
  public StudentDuplicatesInProposalException(String s) {
    super(s);
  }
  
  public StudentDuplicatesInProposalException(String s, Throwable throwable) {
    super(s, throwable);
  }
  
  public StudentDuplicatesInProposalException(Throwable throwable) {
    super(throwable);
  }
  
  public StudentDuplicatesInProposalException(String s, Throwable throwable, boolean b, boolean b1) {
    super(s, throwable, b, b1);
  }
}
