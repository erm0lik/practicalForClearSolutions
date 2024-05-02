package com.example.practicalforclearsolutions.controller;

import com.example.practicalforclearsolutions.Exception.MinAgeException;
import com.example.practicalforclearsolutions.Exception.ValidationException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.DateTimeException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionHandlers {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException exception) {
        Map<String, String> error = new HashMap<>();
        error.put("message", exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidationErrors(ValidationException exception){
        BindingResult result = exception.getBindingResult();
        Map<String, String> error = new HashMap<>();
        result.getFieldErrors().forEach(a -> error.put(a.getField(), a.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    @ExceptionHandler(DateTimeException.class)
    public ResponseEntity<Object> handleDateBetweenError() {
        Map<String, String> error = new HashMap<>();
        error.put("date", "The end date must not be less than the start date ");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    @ExceptionHandler(MinAgeException.class)
    private ResponseEntity<Object> handleMinAgeError(MinAgeException exception) {
        Map<String, String> error = new HashMap<>();
        error.put("age", "Age must be over " + exception.getMinAge() + " years old");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

}
