package ru.practicum.shareit.exception;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import java.util.Map;

@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(ObjectExistenceException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleObjectExistenceException(final ObjectExistenceException e) {
        return Map.of("", e.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleEntityNotFoundException(final EntityNotFoundException e) {
        return Map.of("", e.getMessage());
    }

    @ExceptionHandler({
            ObjectAvailabilityException.class,
            ObjectCreationException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleObjectAvailabilityException(final Exception e) {
        return Map.of("", e.getMessage());
    }

    @ExceptionHandler(ConversionFailedException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Map<String, String> handleConversionFailedException(ConversionFailedException e) {
        return Map.of("error", "Unknown state: " + e.getValue().toString());
    }

    @ExceptionHandler(ObjectAccessException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleObjectAccessException(final ObjectAccessException e) {
        return Map.of("", e.getMessage());
    }
}
