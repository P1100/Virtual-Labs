package it.polito.ai.es2;

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

/**
 * Format HTTP response body (error: {):
 * error: "Conflict"
 * message: "Course c1 cardinalities violated. New cardinalities incompatible with existing teams"
 * path: "/API/courses"
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
    String bodyOfResponse = "{`\"message\":\"Server Error\", \"status\":\"500\", \"error\":\"INTERNAL SERVER ERROR\"}";
    log.severe(ex + " \n " + request);
    return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request); // 500
  }

  @ExceptionHandler(value = {DataAccessException.class, ConstraintViolationException.class, TransactionSystemException.class, RollbackException.class})
  protected ResponseEntity<Object> dataError(RuntimeException ex, WebRequest request) {
    String bodyOfResponse = "{`\"message\":\"Data Error\", \"status\":\"400\", \"error\":\"BAD REQUEST\"}";
    log.warning(ex + " \n " + request);
    return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request); // 400
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
    log.warning("DTO Validation errors: " + errors.values());
    return new ResponseEntity<>(sb.toString(), HttpStatus.UNPROCESSABLE_ENTITY); // 422
  }
}
