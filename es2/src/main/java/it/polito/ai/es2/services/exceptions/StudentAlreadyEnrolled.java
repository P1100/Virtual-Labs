package it.polito.ai.es2.services.exceptions;

public class StudentAlreadyEnrolled extends RuntimeException {
  public StudentAlreadyEnrolled() {
    super("Student already enrolled!");
  }

  public StudentAlreadyEnrolled(String s) {
    super("Student " + s + " already enrolled. ");
  }
}
