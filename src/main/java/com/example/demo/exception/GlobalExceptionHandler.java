package com.example.demo.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import com.example.demo.service.MessageService;
import com.example.demo.util.DemoConstants;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author kaihe
 *
 */

@RequiredArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler {

	@NonNull
	private MessageService messageService;
	
	@ExceptionHandler
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ResponseBody
	public ResponseEntity<Object> handleRecordNotFoundException(RecordNotFoundException ex, WebRequest request) {

		// TODO: depends on requirement and convert to defined error object
		Map<String, Object> errorBody = new LinkedHashMap<>();
		errorBody.put("timestamp", LocalDateTime.now());
		errorBody.put("errorMessage", ex.getLocalizedMessage());

		return new ResponseEntity<>(errorBody, HttpStatus.NOT_FOUND);
	}

	// TODO: handle more case like Validation Exception or Business Exceptions
	@ExceptionHandler
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		List<FieldError> errors = ex.getBindingResult().getFieldErrors();

		Map<String, Object> errorBody = new LinkedHashMap<>();
		errorBody.put("timestamp", LocalDateTime.now());
		errorBody.put("errorMessage", errors.get(0).getDefaultMessage());

		return new ResponseEntity<>(errorBody, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ResponseEntity<Object> handleMissingHeaderException(MissingRequestHeaderException ex) {
		Map<String, Object> errorBody = new LinkedHashMap<>();
		
		String errorMessage = messageService.getMessage(DemoConstants.MISSING_HEADER_ERROR_CODE, ex.getHeaderName());
		errorBody.put("timestamp", LocalDateTime.now());
		errorBody.put("errorMessage", errorMessage);

		return new ResponseEntity<>(errorBody, HttpStatus.BAD_REQUEST);
	}
}
