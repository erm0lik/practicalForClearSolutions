package com.example.practicalforclearsolutions.controller;

import com.example.practicalforclearsolutions.Exception.MinAgeException;
import com.example.practicalforclearsolutions.Exception.ValidationException;
import com.example.practicalforclearsolutions.dto.Dates;
import com.example.practicalforclearsolutions.dto.UsersDto;
import com.example.practicalforclearsolutions.dto.validationGroupInterface.EmptyFieldsValidationGroup;
import com.example.practicalforclearsolutions.service.UsersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UsersController {
    private final UsersService userService;
    @Value("${user.min.age}")
    private int minAge;

    @PostMapping
    public ResponseEntity<Object> createUser(@Valid @RequestBody UsersDto usersDto, BindingResult result)
            throws MinAgeException, ValidationException {
        if (result.hasErrors()) {
            throw new ValidationException(result);
        } else if (Period.between(usersDto.getBirthDate(), LocalDate.now()).getYears() < minAge) {
            throw new MinAgeException(minAge);
        }
        UsersDto savedUser = userService.create(usersDto);
        return ResponseEntity.status(HttpStatus.OK).body(savedUser);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateUserFields(@PathVariable Long userId,
                                                   @Validated(EmptyFieldsValidationGroup.class)
                                                   @RequestBody UsersDto userDto, BindingResult result)
            throws MinAgeException, ValidationException {

        if (result.hasErrors())
            throw new ValidationException(result);
        else if (userDto.getBirthDate() != null && Period.between(userDto.getBirthDate(), LocalDate.now()).getYears() <= minAge)
            throw new MinAgeException(minAge);

        UsersDto updatedUser = userService.updateUsersFields(userId, userDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);


    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.deleteUserById(userId));
    }

    @GetMapping("/dates")
    public ResponseEntity<Object> getAllByDates(@Valid @RequestBody(required = false) Dates dates, BindingResult bindingResult)
            throws ValidationException {
        if (bindingResult.hasErrors())
            throw new ValidationException(bindingResult);
        else if (dates.isValidRange())
            throw new DateTimeException(null);
        List<UsersDto> result = userService.findByBirthDateBetween(dates);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/dates-param")
    public ResponseEntity<Object> getAllByDatesParam(@Valid Dates dates, BindingResult bindingResult)
            throws ValidationException {
        if (bindingResult.hasErrors())
            throw new ValidationException(bindingResult);
        else if (dates.isValidRange())
            throw new DateTimeException(null);
        List<UsersDto> result = userService.findByBirthDateBetween(dates);
        return ResponseEntity.ok(result);
    }

}
