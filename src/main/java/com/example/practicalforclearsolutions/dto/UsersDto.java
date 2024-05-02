package com.example.practicalforclearsolutions.dto;

import com.example.practicalforclearsolutions.dto.validationGroupInterface.EmptyFieldsValidationGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;


import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated({EmptyFieldsValidationGroup.class})
public class UsersDto {

    private long id;


    @NotNull( message = "Must not be empty")
    @Email(groups = EmptyFieldsValidationGroup.class , message = "Please provide a valid email address")
    private String email;

    @NotNull(message = "Must not be empty")
    @Size(groups = EmptyFieldsValidationGroup.class ,min = 1, max = 255, message = "First name must be between 1 and 255 characters")
    private String firstName;

    @NotNull(message = "Must not be empty")
    @Size(groups = EmptyFieldsValidationGroup.class ,min = 1, max = 255, message = "Last name must be between 1 and 255 characters")
    private String lastName;

    @NotNull(message = "The date of birth must not be blank ")
    @Past(groups = EmptyFieldsValidationGroup.class ,message = "Date of birth must be in the past")
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate birthDate;

    private String address;

    @Size(groups = EmptyFieldsValidationGroup.class ,max = 20, message = "Phone number must not exceed 20 characters")
    private String phoneNumber;

}

