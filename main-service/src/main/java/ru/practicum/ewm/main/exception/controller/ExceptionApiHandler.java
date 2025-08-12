package ru.practicum.ewm.main.exception.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import ru.practicum.ewm.main.exception.model.BadRequestException;
import ru.practicum.ewm.main.exception.model.ConflictException;
import ru.practicum.ewm.main.exception.model.ErrorResponse;
import ru.practicum.ewm.main.exception.model.NotFoundException;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
@Slf4j
public class ExceptionApiHandler {

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse entityIsAlreadyExist(ConflictException exception) {
        log.warn("Conflict: {}", exception.getMessage(), exception);
        return new ErrorResponse(
                exception.getMessage(),
                "Entity is already exist",
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse entityIsNotExist(NotFoundException exception) {
        log.warn("Not found: {}", exception.getMessage(), exception);
        return new ErrorResponse(
                exception.getMessage(),
                "Entity is not found",
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse validationErrors(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        String message = fieldErrors.stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .findFirst()
                .orElse("Validation failed");

        log.warn("Validation error: {}", message, e);
        return new ErrorResponse(message, "Validation error", BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        String message = String.format(
                "Required parameter '%s' of type '%s' is missing in the request.",
                e.getParameterName(),
                e.getParameterType()
        );
        log.warn("Missing parameter: {}", message, e);
        return new ErrorResponse(message, "Missing request parameter", BAD_REQUEST);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleHandlerMethodValidationException(HandlerMethodValidationException e) {
        String message = e.getMessage();
        log.warn("Method validation failed: {}", message, e);
        return new ErrorResponse(message, "Method validation error", BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        log.warn("Request body is malformed or missing: {}", e.getMessage(), e);
        return new ErrorResponse(
                "Required request body is missing or malformed.",
                "Malformed JSON",
                BAD_REQUEST
        );
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleBadRequestException(BadRequestException e) {
        log.warn("Bad request: {}", e.getMessage(), e);
        return new ErrorResponse(e.getParameter(), "Bad request", BAD_REQUEST);
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleOtherExceptions(Throwable e) {
        log.error("Unexpected error occurred: {}", e.getMessage(), e);
        return new ErrorResponse(
                e.getMessage(),
                "Internal server error",
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}