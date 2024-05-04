package com.example.practicalforclearsolutions.dto;

import com.example.practicalforclearsolutions.dto.validationGroupInterface.EmptyFieldsValidationGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;


import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated({EmptyFieldsValidationGroup.class})
public class UsersDto {

    private long id;

    @Schema(example = "gmail@gmail.com")
    @NotNull( message = "Must not be empty")
    @Email(groups = EmptyFieldsValidationGroup.class , message = "Please provide a valid email address")
    private String email;

    @Schema(example = "Vladislav")
    @NotNull(message = "Must not be empty")
    @Size(groups = EmptyFieldsValidationGroup.class ,min = 1, max = 255, message = "First name must be between 1 and 255 characters")
    private String firstName;

    @Schema(example = "Ermolenko")
    @NotNull(message = "Must not be empty")
    @Size(groups = EmptyFieldsValidationGroup.class ,min = 1, max = 255, message = "Last name must be between 1 and 255 characters")
    private String lastName;

    @Schema(example = "21.10.2023")
    @NotNull(message = "The date of birth must not be blank ")
    @Past(groups = EmptyFieldsValidationGroup.class ,message = "Date of birth must be in the past")
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate birthDate;

    @Schema(example = "Kiev")
    private String address;

    @Schema(example = "0991234567")
    @Size(groups = EmptyFieldsValidationGroup.class ,max = 20, message = "Phone number must not exceed 20 characters")
    private String phoneNumber;

}

