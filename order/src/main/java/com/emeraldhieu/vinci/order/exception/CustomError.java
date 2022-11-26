package com.emeraldhieu.vinci.order.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * A customer error response to override default error response of Spring Boot.
 * {
 *     "timestamp": "2022-09-09T16:46:14.180+00:00",
 *     "status": 401,
 *     "error": "Unauthorized",
 *     "message": "I WANT TO CUSTOMIZE THIS NOT TO SHOW SENSITIVE INFORMATION",
 *     "path": "/doctor-api/doctors"
 * }
 */
@Builder
@Getter
public class CustomError {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime timestamp;

    private int status;

    private String error;

    private final String message;

    private final String path;
}