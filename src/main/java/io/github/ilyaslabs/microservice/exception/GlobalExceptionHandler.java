package io.github.ilyaslabs.microservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import jakarta.validation.ConstraintViolationException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Global exception handler for the application.
 * Handles various exceptions and returns appropriate HTTP responses.
 *
 * @author Muhammad Ilyas (
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Default constructor for GlobalExceptionHandler.
     * It can be used to initialize any required beans or properties.
     */
    public GlobalExceptionHandler() {

    }

    @Value("${io.github.ilyaslabs.microservice.exception.unhandledExceptionMessage:Something happened that we didn't expect}")
    private String UnhandledExceptionMessage;

    /**
     * Handles HttpResponseException and returns a ResponseEntity with appropriate status code and response body.
     *
     * @param exception The HttpResponseException thrown
     * @return The ResponseEntity with status and response body
     */
    @ExceptionHandler(HttpResponseException.class)
    public ResponseEntity<HttpResponseException.ResponseBody> handleHttpResponseException(HttpResponseException exception) {
        log.debug("Exception occurred while handling http response", exception);
        return ResponseEntity
                .status(exception.getStatus())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                .body(exception.toResponseBody());
    }

    /**
     * Handles NoResourceFoundException and returns a ResponseEntity with not found status and response body.
     *
     * @param exception The NoResourceFoundException thrown
     * @return The ResponseEntity with not found status and response body
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<HttpResponseException.ResponseBody> handleNoResourceFoundException(NoResourceFoundException exception) {
        log.warn("Resource not found: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(HttpResponseException.ofNotFound("404 Not Found")
                .toResponseBody());
    }

    /**
     * Handles generic exceptions and returns a ResponseEntity with internal server error status and response body.
     *
     * @param exception The Exception thrown
     * @return The ResponseEntity with internal server error status and response body
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpResponseException.ResponseBody> handleException(Exception exception) {
        log.error("Unhandled exception", exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(HttpResponseException.ofInternalServerError(
                        UnhandledExceptionMessage
                )
                .toResponseBody());
    }

    /**
     * Handles NoHandlerFoundException and returns a ResponseEntity with not found status and response body.
     *
     * @param exception The NoHandlerFoundException thrown
     * @return The ResponseEntity with not found status and response body
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<HttpResponseException.ResponseBody> handleException(NoHandlerFoundException exception) {
        log.warn("invalid request path for {}", exception.getRequestURL());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(HttpResponseException.ofNotFound()
                .toResponseBody());
    }


    /**
     * Handles MethodArgumentNotValidException and returns a ResponseEntity with appropriate status code and response body.
     *
     * @param exception The MethodArgumentNotValidException thrown
     * @return The ResponseEntity with status and response body
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HttpResponseException.ResponseBody> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {

        log.debug("Validation failed", exception);
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach(
                error -> {
                    FieldError fieldError = (FieldError) error;
                    errors.put(fieldError.getField(), fieldError.getDefaultMessage());
                }
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PROBLEM_JSON_VALUE).body(HttpResponseException.ofBadRequest(
                                errors)
                        .toResponseBody());
    }

    /**
     * Handles ConstraintViolationException and returns a ResponseEntity with appropriate status code and response body.
     *
     * @param exception The ConstraintViolationException thrown
     * @return The ResponseEntity with status and response body
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<HttpResponseException.ResponseBody> handleConstraintViolationException(ConstraintViolationException exception) {
        log.debug("Validation failed", exception);
        Map<String, String> errors = new HashMap<>();
        exception.getConstraintViolations().forEach(violation -> {
            String propertyPath = violation.getPropertyPath().toString();
            String fieldName = propertyPath.substring(propertyPath.lastIndexOf('.') + 1);
            errors.put(fieldName, violation.getMessage());
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                .body(HttpResponseException.ofBadRequest(errors).toResponseBody());
    }

    /**
     * Handles HttpMessageNotReadableException and returns a ResponseEntity with appropriate status code and response body.
     *
     * @param exception The HttpMessageNotReadableException thrown
     * @return The ResponseEntity with status and response body
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<HttpResponseException.ResponseBody> handleException(HttpMessageNotReadableException exception) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                .body(HttpResponseException
                        .ofBadRequest(
                                "Invalid request body"
                        ).toResponseBody()
                );
    }

    /**
     * Handles exceptions of type HandlerMethodValidationException and generates a ResponseEntity
     * containing the details of validation errors and a proper HTTP status code.
     *
     * @param ex The HandlerMethodValidationException instance containing validation error details.
     * @return A ResponseEntity with a BAD_REQUEST status and a body containing validation error messages.
     */
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<HttpResponseException.ResponseBody> handleValidationException(HandlerMethodValidationException ex) {
        log.debug("Validation failed", ex);
        List<ParameterValidationResult> parameterValidationResults = ex.getParameterValidationResults();
        Map<String, String> errors = parameterValidationResults.stream()
                .collect(Collectors.toMap(
                        result -> result.getMethodParameter().getParameterName(),
                        result ->
                                Optional.ofNullable(result.getResolvableErrors().getFirst().getDefaultMessage())
                                        .orElse("Validation failed")
                ));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PROBLEM_JSON_VALUE).body(HttpResponseException.ofBadRequest(
                                errors)
                        .toResponseBody());
    }

}
