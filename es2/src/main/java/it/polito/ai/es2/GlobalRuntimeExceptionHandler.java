package it.polito.ai.es2;

import it.polito.ai.es2.services.exceptions.CourseNotFoundException;
import it.polito.ai.es2.services.exceptions.StudentNotFoundException;
import it.polito.ai.es2.services.exceptions.TeamServiceException;
import lombok.extern.java.Log;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.RollbackException;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@Log
@ControllerAdvice
public class GlobalRuntimeExceptionHandler
    extends ResponseEntityExceptionHandler {
  @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
  protected ResponseEntity<Object> genericError(
      RuntimeException ex, WebRequest request) {
    String bodyOfResponse = "Server Error";
    log.warning(ex.toString() + " \n " + request.toString());
    return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
  }
  
  @ExceptionHandler(value = {DataAccessException.class,
      ConstraintViolationException.class, TransactionSystemException.class, RollbackException.class})
  protected ResponseEntity<Object> dataError(
      RuntimeException ex, WebRequest request) {
    String bodyOfResponse = "Data Error";
    log.warning(ex.toString() + " \n " + request.toString());
    return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }
  
  //  @ResponseStatus(HttpStatus.BAD_REQUEST)
// @ExceptionHandler(MethodArgumentNotValidException.class)
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                HttpHeaders headers, HttpStatus status, WebRequest request) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    log.warning("Validation error list : " + errors.values());
    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }
  
  @ExceptionHandler(value = {TeamServiceException.class})
  protected ResponseEntity<Object> nullParameters(
      RuntimeException ex, WebRequest request) {
    String bodyOfResponse = ex.getMessage();
    log.warning(ex.toString() + " \n " + request.toString());
    return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }
  
  @ExceptionHandler(value = {CourseNotFoundException.class, StudentNotFoundException.class})
  protected ResponseEntity<Object> notFound(
      RuntimeException ex, WebRequest request) {
    String bodyOfResponse = ex.getMessage();
    log.warning(ex.toString() + " \n " + request.toString());
    return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
  }
}