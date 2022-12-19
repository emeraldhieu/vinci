package com.emeraldhieu.vinci.order.exception;

import com.emeraldhieu.vinci.order.logic.sort.InvalidSortOrderException;
import com.fasterxml.jackson.databind.JsonMappingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Exception handler that uses {@link ProblemDetail}.
 */
@Slf4j
@ControllerAdvice // (annotations = RestController.class)
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE) // TODO Investigate why this is needed for NoHandlerFoundException
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;
    private final URI typeUri = URI.create("http://localhost:50001/vinci/types");

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(status);
        problemDetail.setType(typeUri);
        problemDetail.setDetail(exception.getMessage());
        return new ResponseEntity<>(problemDetail, status);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                  HttpHeaders headers, HttpStatusCode status,
                                                                  WebRequest request) {
        BindingResult bindingResult = exception.getBindingResult();
        ProblemDetail problemDetail = ProblemDetail.forStatus(status);
        problemDetail.setType(typeUri);
        problemDetail.setDetail(messageSource.getMessage("invalidRequestBodyArgument", null, null));
        problemDetail.setProperty("fieldErrors", getFieldErrors(bindingResult.getFieldErrors()));
        return new ResponseEntity<>(problemDetail, status);
    }

    private List<String> getFieldErrors(List<FieldError> fieldErrors) {
        return fieldErrors.stream()
            .map(fieldError -> {
                String field = fieldError.getField();
                String message = fieldError.getDefaultMessage();
                return String.format("%s %s", field, message);
            })
            .collect(Collectors.toList());
    }


    @ExceptionHandler(InvalidSortOrderException.class)
    protected ResponseEntity<Object> handleNoHandlerFoundException() {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        problemDetail.setType(typeUri);
        problemDetail.setDetail(messageSource.getMessage("invalidSortOrder", null, null));
        return new ResponseEntity<>(problemDetail, HttpStatus.valueOf(problemDetail.getStatus()));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException exception,
                                                                  HttpHeaders headers, HttpStatusCode status,
                                                                  WebRequest request) {

        ProblemDetail problemDetail = ProblemDetail.forStatus(status);
        problemDetail.setType(typeUri);
        String detailMessage = getInvalidArgumentDetailMessage(exception);
        problemDetail.setDetail(detailMessage);
        return new ResponseEntity<>(problemDetail, status);
    }

    private String getInvalidArgumentDetailMessage(HttpMessageNotReadableException exception) {
        if (exception.getCause() instanceof JsonMappingException jsonMappingException) {
            String fieldName = jsonMappingException.getPath().get(0).getFieldName();
            return messageSource.getMessage("invalidField", new Object[]{fieldName}, null);
        }
        return messageSource.getMessage("invalidJson", null, null);
    }
}
