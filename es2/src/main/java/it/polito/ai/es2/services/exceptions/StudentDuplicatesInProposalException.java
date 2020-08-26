package it.polito.ai.es2.services.exceptions;

public class StudentDuplicatesInProposalException extends RuntimeException {
  public StudentDuplicatesInProposalException() {
  }
  
  public StudentDuplicatesInProposalException(String s) {
    super(s);
  }
}
