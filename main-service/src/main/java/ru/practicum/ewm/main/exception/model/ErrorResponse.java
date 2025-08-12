package ru.practicum.ewm.main.exception.model;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ErrorResponse {
    String error;

    HttpStatus status;
    String description;

    public ErrorResponse(String error, String description, HttpStatus status) {
        this.error = error;
        this.description = description;
        this.status = status;
    }
}