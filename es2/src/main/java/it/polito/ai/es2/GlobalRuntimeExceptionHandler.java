package it.polito.ai.es2;

import it.polito.ai.es2.services.exceptions.CourseNotFoundException;
import it.polito.ai.es2.services.exceptions.StudentNotFoundException;
import it.polito.ai.es2.services.exceptions.TeamServiceException;
import lombok.extern.java.Log;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Log
@ControllerAdvice
public class GlobalRuntimeExceptionHandler
    extends ResponseEntityExceptionHandler {
  @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
  protected ResponseEntity<Object> genericError(
      RuntimeException ex, WebRequest request) {
    String bodyOfResponse = "Server Error";
    log.warning( ex.getMessage()+" \n " + ex.toString() + " \n "  + request.toString());
    return handleExceptionInternal(ex, bodyOfResponse,
        new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
  }
  
  @ExceptionHandler(value = {DataAccessException.class})
  protected ResponseEntity<Object> genericDataError(
      RuntimeException ex, WebRequest request) {
    String bodyOfResponse = "Data Error";
    log.warning( ex.getMessage()+" \n " + ex.toString() + " \n "  + request.toString());
    return handleExceptionInternal(ex, bodyOfResponse,
        new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }
  @ExceptionHandler(value = {TeamServiceException.class})
  protected ResponseEntity<Object> nullParameters(
      RuntimeException ex, WebRequest request) {
    String bodyOfResponse = ex.getMessage();
    log.warning( ex.getMessage()+" \n " + ex.toString() + " \n "  + request.toString());
    return handleExceptionInternal(ex, bodyOfResponse,
        new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }
  
  @ExceptionHandler(value = {CourseNotFoundException.class, StudentNotFoundException.class})
  protected ResponseEntity<Object> notFound(
      RuntimeException ex, WebRequest request) {
    String bodyOfResponse = ex.getMessage();
    log.warning( ex.getMessage()+" \n " + ex.toString() + " \n "  + request.toString());
    return handleExceptionInternal(ex, bodyOfResponse,
        new HttpHeaders(), HttpStatus.NOT_FOUND, request);
  }
}