package io.github.ilyaslabs.microservice.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Map;

/**
 * A custom exception class that represents various types of HTTP response errors.
 * This class extends {@link RuntimeException} and provides methods to create
 * specific instances of {@link HttpResponseException} for common HTTP error statuses
 * such as BAD_REQUEST, UNAUTHORIZED, FORBIDDEN, NOT_FOUND, and INTERNAL_SERVER_ERROR.
 */
public class HttpResponseException extends RuntimeException {

    /**
     * The detail message of the exception.
     */
    private final String message;

    /**
     * The HTTP status of the exception.
     */
    @Getter
    private final HttpStatus status;

    /**
     * A map of field names to error messages indicating validation failures.
     */
    private final Map<String, String> errors;

    /**
     * Additional data related to the exception, if any.
     */
    private final Object data;

    /**
     * The timestamp when the exception was created.
     */
    private final Instant timestamp;

    /**
     * Creates a {@link HttpResponseException} with the specified HTTP status and message.
     *
     * @param status  the HTTP status of the exception
     * @param message the detail message of the exception
     * @return the created HttpResponseException object
     */
    public static HttpResponseException of(HttpStatus status, String message) {
        return new HttpResponseException(status, message, null);
    }

    /**
     * Creates a {@link HttpResponseException} with the specified HTTP status, message, and additional data.
     *
     * @param status  the HTTP status of the exception
     * @param message the detail message of the exception
     * @param errors a map of field names to error messages indicating validation failures
     * @return the created HttpResponseException object
     */
    public static HttpResponseException of(HttpStatus status, String message, Map<String, String> errors) {
        return new HttpResponseException(status, message, errors);
    }

    /**
     * Creates a {@link HttpResponseException} with the specified HTTP status, message, errors, and additional data.
     *
     * @param status  the HTTP status of the exception
     * @param message the detail message of the exception
     * @param errors  a map of field names to error messages indicating validation failures
     * @param data    additional data related to the exception
     * @return the created HttpResponseException object
     */
    public static HttpResponseException of(HttpStatus status, String message, Map<String, String> errors, Object data) {
        return new HttpResponseException(status, message, errors, data);
    }

    /**
     * Creates a {@link HttpResponseException} with an HTTP status code of BAD_REQUEST and the provided message.
     *
     * @param message the detail message
     * @return the created HttpResponseException object
     */
    public static HttpResponseException ofBadRequest(String message) {
        return new HttpResponseException(HttpStatus.BAD_REQUEST, message, null);
    }

    /**
     * Creates a {@link HttpResponseException} with an HTTP status code of BAD_REQUEST,
     * the provided message, and additional data.
     *
     * @param message the detail message
     * @param data    additional data related to the exception
     * @return the created HttpResponseException object
     */
    public static HttpResponseException ofBadRequest(String message, Object data) {
        return new HttpResponseException(HttpStatus.BAD_REQUEST, message, null, data);
    }

    /**
     * Creates a {@link HttpResponseException} with an HTTP status code of BAD_REQUEST.
     * Also add default message "Validation failed" and the provided errors.
     *
     * @param errors a map of field names to error messages indicating validation failures
     * @return the created HttpResponseException object
     */
    public static HttpResponseException ofBadRequest(Map<String, String> errors) {
        return new HttpResponseException(HttpStatus.BAD_REQUEST, "Validation failed", errors, null);
    }

    /**
     * Creates a {@link HttpResponseException} with an HTTP status code of BAD_REQUEST, custom message, and errors.
     *
     * @param message the detail message
     * @param errors  a map of field names to error messages indicating validation failures
     * @return the created HttpResponseException object
     */
    public static HttpResponseException ofBadRequest(String message, Map<String, String> errors) {
        return new HttpResponseException(HttpStatus.BAD_REQUEST, message, errors, null);
    }

    /**
     * Creates a {@link HttpResponseException} with an HTTP status code of UNAUTHORIZED and the provided message.
     *
     * @param message the detail message
     * @return the created HttpResponseException object
     */
    public static HttpResponseException ofUnauthorized(String message) {
        return new HttpResponseException(HttpStatus.UNAUTHORIZED, message, null, null);
    }

    /**
     * Creates a {@link HttpResponseException} with an HTTP status code of FORBIDDEN and the provided message.
     *
     * @param message the detail message
     * @return the created HttpResponseException object
     */
    public static HttpResponseException ofForbidden(String message) {
        return new HttpResponseException(HttpStatus.FORBIDDEN, message, null, null);
    }

    /**
     * Creates a {@link HttpResponseException} with an HTTP status code of UNAUTHORIZED, custom message, and errors.
     *
     * @param message the detail message
     * @param errors  a map of field names to error messages
     * @return the created HttpResponseException object
     */
    public static HttpResponseException ofUnauthorized(String message, Map<String, String> errors) {
        return new HttpResponseException(HttpStatus.UNAUTHORIZED, message, errors, null);
    }

    /**
     * Creates a {@link HttpResponseException} with an HTTP status code of NOT_FOUND and no message.
     *
     * @return the created HttpResponseException object
     */
    public static HttpResponseException ofNotFound() {
        return new HttpResponseException(HttpStatus.NOT_FOUND, null, null, null);
    }

    /**
     * Creates a {@link HttpResponseException} with an HTTP status code of NOT_FOUND and the provided message.
     *
     * @param message the detail message
     * @return the created HttpResponseException object
     */
    public static HttpResponseException ofNotFound(String message) {
        return new HttpResponseException(HttpStatus.NOT_FOUND, message, null, null);
    }

    /**
     * Creates a {@link HttpResponseException} with an HTTP status code of INTERNAL_SERVER_ERROR and the provided message.
     *
     * @param message the detail message
     * @return the created HttpResponseException object
     */
    public static HttpResponseException ofInternalServerError(String message) {
        return new HttpResponseException(HttpStatus.INTERNAL_SERVER_ERROR, message, null, null);
    }

    /**
     * Constructs an HttpResponseException with the specified HTTP status, message, and errors.
     *
     * @param status  the HTTP status of the exception
     * @param message the detail message of the exception
     * @param errors  the map of field-specific error messages
     */
    private HttpResponseException(HttpStatus status, String message, Map<String, String> errors) {
        super(message);
        this.message = message;
        this.status = status;
        this.errors = errors;
        this.timestamp = Instant.now();
        this.data = null;
    }

    /**
     * Constructs an HttpResponseException with the specified HTTP status, message, errors, and additional data.
     * @param status the HTTP status of the exception
     * @param message the detail message of the exception
     * @param errors the map of field-specific error messages
     * @param data additional data related to the exception
     */
    private HttpResponseException(HttpStatus status, String message, Map<String, String> errors, Object data) {
        super(message);
        this.message = message;
        this.status = status;
        this.errors = errors;
        this.timestamp = Instant.now();
        this.data = data;
    }

    /**
     * ResponseBody is a record that represents the body of the HTTP response
     * @param message the message to be included in the response
     * @param fields a map of field names to error messages indicating validation failures
     * @param timestamp the timestamp of when the response was created
     * @param data additional data related to the exception
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ResponseBody(
            String message,
            Map<String, String> fields,
            Instant timestamp,
            Object data
    ) {
    }

    /**
     * Converts the exception details to a ResponseBody object.
     *
     * @return A ResponseBody containing the message, errors, and timestamp of the exception.
     */
    public ResponseBody toResponseBody() {
        return new ResponseBody(message, errors, timestamp, data);
    }
}
