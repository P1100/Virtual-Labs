package it.polito.ai.es2.services;

import it.polito.ai.es2.services.exceptions.CourseNotFoundException;
import it.polito.ai.es2.services.exceptions.StudentNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler
    extends ResponseEntityExceptionHandler {
  @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
  protected ResponseEntity<Object> genericError(
      RuntimeException ex, WebRequest request) {
    String bodyOfResponse = "Generic Error";
    return handleExceptionInternal(ex, bodyOfResponse,
        new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
  }
  
  @ExceptionHandler(value = {CourseNotFoundException.class, StudentNotFoundException.class})
  protected ResponseEntity<Object> notFound(
      RuntimeException ex, WebRequest request) {
    String bodyOfResponse = "Not Found";
    return handleExceptionInternal(ex, bodyOfResponse,
        new HttpHeaders(), HttpStatus.PRECONDITION_FAILED, request);
  }
}