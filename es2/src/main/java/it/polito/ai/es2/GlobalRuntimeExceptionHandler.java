package it.polito.ai.es2;

import it.polito.ai.es2.services.exceptions.CourseNotFoundException;
import it.polito.ai.es2.services.exceptions.StudentNotFoundException;
import it.polito.ai.es2.services.exceptions.TeamServiceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalRuntimeExceptionHandler
    extends ResponseEntityExceptionHandler {
  @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
  protected ResponseEntity<Object> genericError(
      RuntimeException ex, WebRequest request) {
    String bodyOfResponse = "Generic Server Error";
    return handleExceptionInternal(ex, bodyOfResponse,
        new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
  }
  
  @ExceptionHandler(value = {TeamServiceException.class})
  protected ResponseEntity<Object> nullParameters(
      RuntimeException ex, WebRequest request) {
    String bodyOfResponse = ex.getMessage();
    return handleExceptionInternal(ex, bodyOfResponse,
        new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }
  
  @ExceptionHandler(value = {CourseNotFoundException.class, StudentNotFoundException.class})
  protected ResponseEntity<Object> notFound(
      RuntimeException ex, WebRequest request) {
    String bodyOfResponse = ex.getMessage();
    return handleExceptionInternal(ex, bodyOfResponse,
        new HttpHeaders(), HttpStatus.NOT_FOUND, request);
  }
}