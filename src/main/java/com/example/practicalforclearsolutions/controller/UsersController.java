package com.example.practicalforclearsolutions.controller;

import com.example.practicalforclearsolutions.Exception.MinAgeException;
import com.example.practicalforclearsolutions.Exception.ValidationException;
import com.example.practicalforclearsolutions.dto.Dates;
import com.example.practicalforclearsolutions.dto.UsersDto;
import com.example.practicalforclearsolutions.dto.validationGroupInterface.EmptyFieldsValidationGroup;
import com.example.practicalforclearsolutions.service.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
@Tag(name = "Users", description = "Controller for user management")
public class UsersController {
    private final UsersService userService;
    @Value("${user.min.age}")
    private int minAge;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Create user",
            description = "User creation , data validation and age verification."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = UsersDto.class))),
            @ApiResponse(responseCode = "400" ,description = "Bad Request")
    })
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

    @PutMapping(path = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "User update ",
            description = "Full or partial user update, data and age validation. "
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = UsersDto.class))),
            @ApiResponse(responseCode = "400" ,description = "Bad Request")
    })
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


    @DeleteMapping(path = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Deleting a user ",
            description = "Deleting a user by its unique identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = UsersDto.class))),
            @ApiResponse(responseCode = "404" ,description = "Not Found")
    })
    public ResponseEntity<Object> deleteById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.deleteUserById(userId));
    }

    @GetMapping(path = "/dates", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Getting the list of users",
            description = "Getting the list of users by their date of birth, dates are passed through JSON"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = UsersDto.class))),
            @ApiResponse(responseCode = "400" ,description = "Bad Request"),
            @ApiResponse(responseCode = "404" ,description = "Not Found")
    })
    public ResponseEntity<Object> getAllByDates(@Valid @RequestBody(required = false) Dates dates, BindingResult bindingResult)
            throws ValidationException {
        if (bindingResult.hasErrors())
            throw new ValidationException(bindingResult);
        else if (dates.isValidRange())
            throw new DateTimeException(null);
        List<UsersDto> result = userService.findByBirthDateBetween(dates);
        return ResponseEntity.ok(result);
    }

    @GetMapping(path = "/dates-param", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Getting the list of users(URL parameters)",
            description = "Getting the list of users by their date of birth, dates are passed through URL parameters"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = UsersDto.class))),
            @ApiResponse(responseCode = "400" ,description = "Bad Request"),
            @ApiResponse(responseCode = "404" ,description = "Not Found")
    })
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
