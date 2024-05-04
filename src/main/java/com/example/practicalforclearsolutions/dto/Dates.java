package com.example.practicalforclearsolutions.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(example = "2000.01.01")
    @NotNull(message = "The date of birth must not be blank ")
    @Past(message = "Date of birth must be in the past")
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate fromDate;
    @Schema(example = "2020.01.01")
    @NotNull(message = "The date of birth must not be blank ")
    @Past(message = "Date of birth must be in the past")
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate toDate;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    public boolean isValidRange() {
        return fromDate != null && toDate != null && toDate.isBefore(fromDate);
    }
}
