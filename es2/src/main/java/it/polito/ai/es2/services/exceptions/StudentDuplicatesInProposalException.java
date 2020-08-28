package it.polito.ai.es2.services.exceptions;

public class StudentDuplicatesInProposalException extends RuntimeException {
  public StudentDuplicatesInProposalException() {
    super("Student duplicates in proposal.");
  }

  public StudentDuplicatesInProposalException(String s) {
    super("Student duplicates in proposal. (" + s + ")");
  }
}
