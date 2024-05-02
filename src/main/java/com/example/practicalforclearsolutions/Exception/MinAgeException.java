package com.example.practicalforclearsolutions.Exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class MinAgeException extends Exception{
    private  int minAge;
}
