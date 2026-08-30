package com.agtual.challengetracker.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.agtual.challengetracker.exception.AlreadyExistsException;
import com.agtual.challengetracker.exception.ForbiddenException;
import com.agtual.challengetracker.exception.NotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AlreadyExistsException.class)
    public ProblemDetail handleAlreadyExists(AlreadyExistsException ex) {
        return buildProblemDetail(HttpStatus.CONFLICT, ex.getMessage(), AlreadyExistsException.TITLE);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ProblemDetail handleForbidden(ForbiddenException ex) {
        return buildProblemDetail(HttpStatus.FORBIDDEN, ex.getMessage(), ForbiddenException.TITLE);
    }

    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleNotFound(NotFoundException ex) {
        return buildProblemDetail(HttpStatus.NOT_FOUND, ex.getMessage(), NotFoundException.TITLE);
    }

    // Fallback for any other unhandled RuntimeException
    @ExceptionHandler(RuntimeException.class)
    public ProblemDetail handleRuntimeException(RuntimeException ex) {
        return buildProblemDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred. Please try again later.",
                "Internal Server Error");
    }

    private ProblemDetail buildProblemDetail(HttpStatus status, String message, String title) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, message);
        problemDetail.setTitle(title);
        return problemDetail;
    }
}
