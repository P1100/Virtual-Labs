package it.polito.ai.es2.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class StudentNotFoundException extends RuntimeException {
  public StudentNotFoundException() {
    super("Student not found.");
  }

  public StudentNotFoundException(String s) {
    super("Students " + s + " not found. ".trim());
  }
}
