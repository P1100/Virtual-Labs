package it.polito.ai.es2.services.exceptions;

public class StudentNotEnrolledException extends RuntimeException {
  public StudentNotEnrolledException() {
    super("Student not enrolled.");
  }

  public StudentNotEnrolledException(String s) {
    super("Student " + s + " not enrolled. ");
  }
}
