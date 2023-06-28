package ru.practicum.shareit.exception;


import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.Map;
import java.util.Objects;


@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationException(ConstraintViolationException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler()
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Map<String, String> handleConversionFailedException(ConversionFailedException e) {
        return Map.of("error", "Unknown state: " + Objects.requireNonNull(e.getValue()));
    }
}
