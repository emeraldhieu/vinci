package com.emeraldhieu.vinci.payment.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

/**
 * Handle exceptions in one place.
 * Purposes:
 * 1) Reuse default Spring Boot's error response
 * {
 * "timestamp": "2022-09-10 12:15:58",
 * "status": 400,
 * "error": "Bad Request",
 * "message": "Invalid JSON. Check your JSON.",
 * "path": "/foo/bar"
 * }
 * 2) Hide implementation details by overriding "message".
 * 3) Log detailed error for developers to know what's going on.
 * <p>
 * See https://www.baeldung.com/exception-handling-for-rest-with-spring#controlleradvice
 * See https://mkyong.com/spring-boot/spring-rest-error-handling-example
 */
@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private final HttpServletRequest httpServletRequest;
    private final MessageSource messageSource;

    @ExceptionHandler({
        NotFoundException.class,
    })
    public ResponseEntity<CustomError> handleNotFoundException(RuntimeException exception, HttpServletRequest request) {
        CustomError customError = CustomError.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.NOT_FOUND.value())
            .error(HttpStatus.NOT_FOUND.getReasonPhrase())
            .message(exception.getMessage())
            .path(request.getContextPath() + request.getServletPath())
            .build();

        return new ResponseEntity<>(customError, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException exception,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error(exception.getMessage());

        CustomError customError = CustomError.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .error(HttpStatus.BAD_REQUEST.getReasonPhrase())

            // Show a common error for clients. Avoid leaking internal details.
            .message(messageSource.getMessage("invalidJson", null, null))

            .path(request.getContextPath() + httpServletRequest.getServletPath())
            .build();

        return new ResponseEntity<>(customError, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        // Log detailed message for developers to know what's going on.
        log.error(exception.getMessage());

        String message = String.format("Resource %s not found", exception.getRequestURL());

        CustomError customError = CustomError.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.NOT_FOUND.value())
            .error(HttpStatus.NOT_FOUND.getReasonPhrase())

            // Show a common error for clients. Avoid leaking internal details.
            .message(message)

            .path(request.getContextPath() + httpServletRequest.getServletPath())
            .build();

        return new ResponseEntity<>(customError, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception exception, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        // Log detailed message for developers to know what's going on.
        log.error(exception.getMessage());

        CustomError customError = CustomError.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())

            // Show a common error for clients. Avoid leaking internal details.
            .message(messageSource.getMessage("internalServerError", null, null))

            .path(request.getContextPath() + httpServletRequest.getServletPath())
            .build();

        return new ResponseEntity<>(customError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        // Log detailed message for developers to know what's going on.
        log.error(exception.getMessage());

        String message = getMessage(exception);

        CustomError customError = CustomError.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .error(HttpStatus.BAD_REQUEST.getReasonPhrase())

            // Show a common error for clients. Avoid leaking internal details.
            .message(message)

            .path(request.getContextPath() + httpServletRequest.getServletPath())
            .build();

        return new ResponseEntity<>(customError, HttpStatus.BAD_REQUEST);
    }

    private String getMessage(TypeMismatchException exception) {
        if (exception instanceof MethodArgumentTypeMismatchException) {
            String name = ((MethodArgumentTypeMismatchException) exception).getName();
            String type = getType(exception);
            return String.format("%s must be %s", name, type);
        }
        String name = exception.getPropertyName();
        String type = getType(exception);
        return String.format("%s must be %s", name, type);
    }

    private String getType(TypeMismatchException exception) {
        if (exception.getRequiredType() == Double.class) {
            return exception.getRequiredType().getSimpleName().toLowerCase();
        }
        return exception.getRequiredType().getSimpleName();
    }

    @ExceptionHandler
    protected ResponseEntity<Object> handleHttpMessageNotReadable(BadRequestException exception, HttpServletRequest request) {
        CustomError customError = CustomError.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
            .message(exception.getMessage()) // This message comes from feign client
            .path(request.getContextPath() + request.getServletPath())
            .build();

        return new ResponseEntity<>(customError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    protected ResponseEntity<Object> handleTooManyRequestException(TooManyRequestException exception) {
        CustomError customError = CustomError.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.TOO_MANY_REQUESTS.value())
            .error(HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase())
            .message(exception.getMessage()) // This message comes from feign client
            .path(httpServletRequest.getContextPath() + httpServletRequest.getServletPath())
            .build();

        return new ResponseEntity<>(customError, HttpStatus.TOO_MANY_REQUESTS);
    }
}
