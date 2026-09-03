package com.agtual.challengetracker.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.agtual.challengetracker.exception.AlreadyExistsException;
import com.agtual.challengetracker.exception.ForbiddenException;
import com.agtual.challengetracker.exception.NotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AlreadyExistsException.class)
    public ProblemDetail handleAlreadyExists(AlreadyExistsException ex) {
        return buildProblemDetail(AlreadyExistsException.HTTP_STATUS, ex.getMessage(), AlreadyExistsException.TITLE);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ProblemDetail handleForbidden(ForbiddenException ex) {
        return buildProblemDetail(ForbiddenException.HTTP_STATUS, ex.getMessage(), ForbiddenException.TITLE);
    }

    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleNotFound(NotFoundException ex) {
        return buildProblemDetail(NotFoundException.HTTP_STATUS, ex.getMessage(), NotFoundException.TITLE);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationException(MethodArgumentNotValidException ex) {
        return buildProblemDetail(HttpStatus.BAD_REQUEST, "Method argument not valid", "Invalid request");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        return buildProblemDetail(HttpStatus.CONFLICT, ex.getMessage(), "Invalid Data");
    }

    // Fallback for any other unhandled Exception
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleException(Exception ex) {
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
