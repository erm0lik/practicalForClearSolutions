package com.example.practicalforclearsolutions.Exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ValidationException extends Exception{
    private BindingResult bindingResult;
}
