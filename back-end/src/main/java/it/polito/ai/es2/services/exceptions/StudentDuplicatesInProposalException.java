package it.polito.ai.es2.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class StudentDuplicatesInProposalException extends RuntimeException {
  public StudentDuplicatesInProposalException() {
    super("Student duplicates in proposal.");
  }

  public StudentDuplicatesInProposalException(String s) {
    super("Duplicate students in proposal: " + s);
  }
}
