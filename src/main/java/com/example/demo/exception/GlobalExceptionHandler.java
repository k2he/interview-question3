package com.example.demo.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

/**
 * @author kaihe
 *
 */

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  @ResponseBody
  public ResponseEntity<Object> handleRecordNotFoundExceptionException(RecordNotFoundException ex,
      WebRequest request) {

    // TODO: depends on requirement and convert to defined error object
    Map<String, Object> errorBody = new LinkedHashMap<>();
    errorBody.put("timestamp", LocalDateTime.now());
    errorBody.put("errorMessage", ex.getLocalizedMessage());

    return new ResponseEntity<>(errorBody, HttpStatus.NOT_FOUND);
  }

  // TODO: handle more case like Validation Exception or Business Exceptions
}
