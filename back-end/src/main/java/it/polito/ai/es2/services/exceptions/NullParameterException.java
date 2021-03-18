package it.polito.ai.es2.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class NullParameterException extends RuntimeException {
  public NullParameterException() {
    super("Null parameters.");
  }

  public NullParameterException(String commaSeparatedNullParametersName) {
    super("Null parameters. (" + commaSeparatedNullParametersName + ")");
  }

  public NullParameterException(Long commaSeparatedNullParametersName) {
    super("Null parameters. (" + commaSeparatedNullParametersName + ")");
  }
}
