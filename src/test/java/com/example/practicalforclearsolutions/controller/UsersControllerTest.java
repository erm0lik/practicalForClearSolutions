package com.example.practicalforclearsolutions.controller;

import com.example.practicalforclearsolutions.Exception.MinAgeException;
import com.example.practicalforclearsolutions.Exception.ValidationException;
import com.example.practicalforclearsolutions.dto.Dates;
import com.example.practicalforclearsolutions.dto.UsersDto;
import com.example.practicalforclearsolutions.service.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsersControllerTest {
    @Mock
    private UsersService usersService;
    @Mock
    private BindingResult bindingResult;
    @InjectMocks
    private UsersController usersController;
    private final int minAgeExample;

    public UsersControllerTest() throws IOException {
        Properties prop = new Properties();
        prop.load(new FileInputStream("src/main/resources/application.properties"));
        minAgeExample = Integer.parseInt((String) prop.get("user.min.age"));
    }

    @BeforeEach
    private void updateMinAgeInuUsersController() throws NoSuchFieldException, IllegalAccessException {
        Field field = usersController.getClass().getDeclaredField("minAge");
        field.setAccessible(true);
        field.set(usersController, minAgeExample);
    }


    @Test
    void createUser_whenDtoIsNotValid_takeValidationException() {
        UsersDto usersDto = mock(UsersDto.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        assertThrows(ValidationException.class, () -> usersController.createUser(usersDto, bindingResult));
    }

    @Test
    void createUser_whenBirthDateIsNotValid_takeMinAgeException() {
        UsersDto usersDto = new UsersDto();
        usersDto.setBirthDate(LocalDate.now().minusYears(minAgeExample - 1));
        when(bindingResult.hasErrors()).thenReturn(false);

        assertThrows(MinAgeException.class, () -> usersController.createUser(usersDto, bindingResult));
    }

    @Test
    void createUser_IsValid_takeSaveAndTakeUserDTO() throws MinAgeException, ValidationException {
        UsersDto usersDto = new UsersDto(1, "example@gmail.com", "Example", "Example",
                LocalDate.of(2000, 1, 1), "Example", "Example");
        when(usersService.create(usersDto)).thenReturn(usersDto);

        var responseEntity = usersController.createUser(usersDto, bindingResult);

        verify(usersService, times(1)).create(usersDto);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(usersDto, responseEntity.getBody());

    }

    @Test
    void updateUserFields_whenDtoIsNotValid_takeValidationException() {
        UsersDto usersDto = new UsersDto();
        when(bindingResult.hasErrors()).thenReturn(true);

        assertThrows(ValidationException.class, () -> usersController.createUser(usersDto, bindingResult));

    }

    @Test
    void updateUserFields_whenBirthDateIsNotValid_takeMinAgeException() {
        UsersDto usersDto = new UsersDto();
        usersDto.setBirthDate(LocalDate.now().minusYears(minAgeExample - 1));
        when(bindingResult.hasErrors()).thenReturn(false);

        //To avoid loading SpringContext since minAge is loaded from the file via @Value.

        assertThrows(MinAgeException.class, () -> usersController.updateUserFields(usersDto.getId(), usersDto, bindingResult));
    }

    @Test
    void updateUserFields_whenUserFound_takeUserUpdateDto() throws MinAgeException, ValidationException {
        UsersDto usersDto = new UsersDto();
        usersDto.setId(1L);
        usersDto.setFirstName("Vlad");

        UsersDto usersDtoUpdate = new UsersDto();
        usersDto.setFirstName("Vladik");

        when(bindingResult.hasErrors()).thenReturn(false);
        when(usersService.updateUsersFields(usersDto.getId(), usersDtoUpdate)).thenReturn(usersDtoUpdate);

        ResponseEntity<Object> responseEntity = usersController.updateUserFields(usersDto.getId(), usersDtoUpdate, bindingResult);

        verify(usersService).updateUsersFields(usersDto.getId(), usersDtoUpdate);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(usersDtoUpdate, responseEntity.getBody());
    }

    @Test
    void deleteById_whenUserFound_takeUserDro() {
        UsersDto usersDto = new UsersDto();
        usersDto.setId(1L);
        usersDto.setFirstName("Vlad");
        when(usersService.deleteUserById(usersDto.getId())).thenReturn(usersDto);

        ResponseEntity<Object> responseEntity = usersController.deleteById(usersDto.getId());

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(usersDto, responseEntity.getBody());
    }

    @Test
    void getAllByDates_whenDateIsNotValid_takeValidationException() {
        Dates dates = new Dates();
        when(bindingResult.hasErrors()).thenReturn(true);

        assertThrows(ValidationException.class, () -> usersController.getAllByDates(dates, bindingResult));
        assertThrows(ValidationException.class, () -> usersController.getAllByDatesParam(dates, bindingResult));
    }

    @Test
    void getAllByDates_whenIsNotValidRange_takeDateTimeException() {
        Dates dates = new Dates();
        dates.setFromDate(LocalDate.of(2000, 1, 1));
        dates.setToDate(LocalDate.of(1950, 1, 1));

        assertThrows(DateTimeException.class, () -> usersController.getAllByDates(dates, bindingResult));
        assertThrows(DateTimeException.class, () -> usersController.getAllByDatesParam(dates, bindingResult));
    }

    @Test
    void getAllByDates_whenAllIsOk_takeListUsers() throws ValidationException {
        Dates dates = new Dates();
        dates.setFromDate(LocalDate.of(2000, 1, 1));
        dates.setToDate(LocalDate.of(2020, 1, 1));
        UsersDto usersDto1 = new UsersDto();
        usersDto1.setFirstName("Vlad");
        usersDto1.setBirthDate(LocalDate.of(2003, 10, 21));
        UsersDto usersDto2 = new UsersDto();
        usersDto2.setFirstName("Nikita");
        usersDto2.setBirthDate(LocalDate.of(2004, 10, 21));
        when(bindingResult.hasErrors()).thenReturn(false);

        List<UsersDto> dtoList = List.of(usersDto1, usersDto2);
        when(usersService.findByBirthDateBetween(dates)).thenReturn(dtoList);

        ResponseEntity<Object> responseEntity1 = usersController.getAllByDates(dates, bindingResult);
        ResponseEntity<Object> responseEntity2 = usersController.getAllByDatesParam(dates, bindingResult);

        assertNotNull(responseEntity1);
        assertEquals(HttpStatus.OK, responseEntity1.getStatusCode());
        assertEquals(dtoList, responseEntity1.getBody());

        assertNotNull(responseEntity2);
        assertEquals(HttpStatus.OK, responseEntity2.getStatusCode());
        assertEquals(dtoList, responseEntity2.getBody());

    }
}
