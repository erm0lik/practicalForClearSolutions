package com.example.practicalforclearsolutions.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Dates {
    @NotNull(message = "The date of birth must not be blank ")
    @Past(message = "Date of birth must be in the past")
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate fromDate;
    @NotNull(message = "The date of birth must not be blank ")
    @Past(message = "Date of birth must be in the past")
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate toDate;


    public boolean isValidRange() {
        return fromDate != null && toDate != null && toDate.isBefore(fromDate);
    }
}
