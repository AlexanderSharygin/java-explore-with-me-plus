package ru.practicum.ewm.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@RestControllerAdvice(basePackages = {"ru.practicum.ewm.stats.controller"})
@Slf4j
public class ExceptionHandler {
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ApiError handleAll(final Exception ex) {
        log.error(Arrays.toString(ex.getStackTrace()));
        return new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), "Something went wrong");
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIncorrectParameterException(final BadRequestException e) {
        log.warn(e.getMessage());

        return new ErrorResponse(e.getParameter(), "Bad request", HttpStatus.BAD_REQUEST.toString());
    }
}
