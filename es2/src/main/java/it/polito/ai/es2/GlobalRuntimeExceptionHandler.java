package it.polito.ai.es2;

import lombok.extern.java.Log;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

/**
 * Format HTTP response body (error: {):
 * error: "Conflict"
 * message: "Course c1 cardinalities violated. New cardinalities incompatible with existing teams"
 * path: "/api/courses"
 * status: 409
 * timestamp: 1598655328395
 * }
 */
@Log
@ControllerAdvice
public class GlobalRuntimeExceptionHandler
    extends ResponseEntityExceptionHandler {
  @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
  protected ResponseEntity<Object> genericJavaError(RuntimeException ex, WebRequest request) {
    String bodyOfResponse = "{\"message\":\"Server Error \", \"status\":\"500\", \"error\":\"INTERNAL SERVER ERROR\"}";
    log.severe(ex + " \n " + request);
    return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request); // 500
  }

  @ExceptionHandler(value = {ConstraintViolationException.class})
  protected ResponseEntity<Object> constraintError(RuntimeException ex, WebRequest request) {
    String bodyOfResponse = "{\"message\":\"Constraint Violation Error\", \"status\":\"400\", \"error\":\"BAD REQUEST\"}";
    log.severe(ex + " \n " + request);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    return handleExceptionInternal(ex, bodyOfResponse, headers, HttpStatus.BAD_REQUEST, request); // 400
  }
// TODO: uncomment before release

//  @ExceptionHandler(value = {DataAccessException.class, TransactionSystemException.class, RollbackException.class})
//  protected ResponseEntity<Object> dataError(RuntimeException ex, WebRequest request) {
//    String bodyOfResponse = "{\"message\":\"JPA Data Error\", \"status\":\"500\", \"error\":\"INTERNAL_SERVER_ERROR\"}";
//    log.severe(ex + " \n " + request);
//    HttpHeaders headers = new HttpHeaders();
//    headers.setContentType(MediaType.APPLICATION_JSON);
//    return handleExceptionInternal(ex, bodyOfResponse, headers, HttpStatus.INTERNAL_SERVER_ERROR, request); // 400
//  }

  @ExceptionHandler(value = {UsernameNotFoundException.class})
  protected ResponseEntity<Object> loginFailed(RuntimeException ex, WebRequest request) {
    String bodyOfResponse = "{\"message\":\"User not found in authentication!\", \"status\":\"401\", \"error\":\"UNAUTHORIZED\"}";
    log.severe(ex + " \n " + request);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    return handleExceptionInternal(ex, bodyOfResponse, headers, HttpStatus.UNAUTHORIZED, request); // 400
  }

  /**
   * DTO Validation errors (override of ResponseEntityExceptionHandler)
   **/
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                HttpHeaders headers, HttpStatus status, WebRequest request) {
    Map<String, String> errors = new HashMap<>();
    StringBuilder sb = new StringBuilder();
    sb.append("{\"message\":\"Validation errors in fields:");
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
      sb.append(" ").append(fieldName);
    });
    sb.append("\"");
    sb.append(", \"status\":\"422\", \"error\":\"Unprocessable Entity\"}");
    log.warning("DTO Validation errors: ");
    for (Map.Entry<String, String> entry : errors.entrySet()) {
      log.warning(entry.getKey() + entry.getValue());
    }
    return new ResponseEntity<>(sb.toString(), HttpStatus.UNPROCESSABLE_ENTITY); // 422
  }
}
