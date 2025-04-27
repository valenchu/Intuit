package com.challenge.Intuit.errors;

import io.swagger.v3.oas.annotations.Hidden;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {
	private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	// Manejo de errores de validación
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, Object> response = new HashMap<>();
		Map<String, Object> errors = new HashMap<>();

		ex.getBindingResult().getAllErrors().forEach(error -> {
			String fieldName = error instanceof FieldError
					? ((FieldError) error).getField()
					: error.getObjectName();

			errors.put(fieldName, error.getDefaultMessage());
		});

		response.put("timestamp", new Date());
		response.put("status", HttpStatus.BAD_REQUEST.value());
		response.put("errors", errors);
		handleErrorLog("Validation error", errors);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	// Manejo de recursos no encontrados
	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<Map<String, Object>> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
		Map<String, Object> response = new HashMap<>();
		response.put("timestamp", new Date());
		response.put("status", HttpStatus.NOT_FOUND.value());
		response.put("error", HttpStatus.NOT_FOUND.getReasonPhrase());
		response.put("message", ex.getLocalizedMessage());
		response.put("path", getCleanPath(request));

		handleErrorLog("Resource not found", response);
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	// Manejo de errores genéricos
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<Map<String, Object>> handleAllExceptions(Exception ex, WebRequest request) {
		Map<String, Object> response = new HashMap<>();
		response.put("timestamp", new Date());
		response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
		response.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
		response.put("message", ex.getLocalizedMessage());
		response.put("path", getCleanPath(request));

		handleErrorLog("Internal server error", Map.of("message", ex.getMessage()));
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private String getCleanPath(WebRequest request) {
		return request.getDescription(false).replace("uri=", "");
	}

	private static void handleErrorLog(String errorType, Map<String, ?> details) {
		LOG.error("{} - Details: {}", errorType, details);
	}
}
