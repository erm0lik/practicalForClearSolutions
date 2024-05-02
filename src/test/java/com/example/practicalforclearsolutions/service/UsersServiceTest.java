package com.example.practicalforclearsolutions.service;

import com.example.practicalforclearsolutions.dto.Dates;
import com.example.practicalforclearsolutions.dto.UsersDto;
import com.example.practicalforclearsolutions.entity.Users;
import com.example.practicalforclearsolutions.repository.UsersRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsersServiceTest {
    @Mock
    private UsersRepository usersRepository;
    @InjectMocks
    private UsersService usersService;

    @Test
    void findByBirthDateBetween_whenResultEmpty() {
        Dates dates = new Dates();
        dates.setFromDate(LocalDate.of(1980, 1, 1));
        dates.setToDate(LocalDate.of(1985, 1, 1));

        when(usersRepository.findByBirthDateBetween(dates.getFromDate(), dates.getToDate())).thenReturn(new ArrayList<>());

        assertThrows(EntityNotFoundException.class, () -> usersService.findByBirthDateBetween(dates));
    }

    @Test
    void findByBirthDateBetween_whenResultNotEmpty() {
        Dates dates = new Dates();
        dates.setFromDate(LocalDate.of(1985, 1, 1));
        dates.setToDate(LocalDate.of(1995, 1, 1));


        Users user = new Users();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setBirthDate(LocalDate.of(1990, 1, 1));
        user.setAddress("123 Main St");
        user.setPhoneNumber("1234567890");
        UsersDto usersDto = new UsersDto();
        usersDto.setId(1L);
        usersDto.setEmail("test@example.com");
        usersDto.setFirstName("John");
        usersDto.setLastName("Doe");
        usersDto.setBirthDate(LocalDate.of(1990, 1, 1));
        usersDto.setAddress("123 Main St");
        usersDto.setPhoneNumber("1234567890");

        List<Users> usersList = List.of(user);
        when(usersRepository.findByBirthDateBetween
                (dates.getFromDate(), dates.getToDate())).thenReturn(usersList);

        List<UsersDto> usersDtoList = usersService.findByBirthDateBetween(dates);
        assertNotNull(usersDtoList);
        assertEquals(usersDto, usersDtoList.get(0));
    }

    @Test
    void testCreate() {

        UsersDto createDto = new UsersDto();
        createDto.setId(1L);
        createDto.setEmail("test@example.com");
        createDto.setFirstName("John");
        createDto.setLastName("Doe");
        createDto.setBirthDate(LocalDate.of(1990, 1, 1));
        createDto.setAddress("123 Main St");
        createDto.setPhoneNumber("1234567890");

        Users savedUser = new Users();
        savedUser.setId(1L);
        savedUser.setEmail("test@example.com");
        savedUser.setFirstName("John");
        savedUser.setLastName("Doe");
        savedUser.setBirthDate(LocalDate.of(1990, 1, 1));
        savedUser.setAddress("123 Main St");
        savedUser.setPhoneNumber("1234567890");

        when(usersRepository.save(Mockito.any(Users.class))).thenReturn(savedUser);

        UsersDto result = usersService.create(createDto);

        Mockito.verify(usersRepository, Mockito.times(1)).save(Mockito.any(Users.class));

        assertNotNull(result);
        assertEquals(result, createDto);
    }

    @Test
    void updateUsersFields_whenUserNotFound() {
        when(usersRepository.findById(anyLong()))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class,
                () -> usersService.updateUsersFields(1, new UsersDto()));
    }

    @Test
    void updateUsersFields_whenUserFound() {
        Users user = new Users();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setBirthDate(LocalDate.of(1990, 1, 1));
        user.setAddress("123 Main St");
        user.setPhoneNumber("1234567890");

        when(usersRepository.findById(anyLong())).thenReturn(Optional.of(user));

        UsersDto updateData = new UsersDto();
        updateData.setFirstName("Genadi");

        UsersDto result = usersService.updateUsersFields(1, updateData);

        assertEquals(result.getFirstName(), updateData.getFirstName());
    }

    @Test
    void deleteUserById_whenUserNotFound() {
        when(usersRepository.findById(anyLong()))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class,
                () -> usersService.deleteUserById(1));
    }

    @Test
    void deleteUserById_whenUserFound() {
        Users user = new Users();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setBirthDate(LocalDate.of(1990, 1, 1));
        user.setAddress("123 Main St");
        user.setPhoneNumber("1234567890");


        when(usersRepository.findById(anyLong())).thenReturn(Optional.of(user));

        UsersDto deletedUserDto = usersService.deleteUserById(1L);

        assertNotNull(deletedUserDto);
        assertEquals(user.getId(), deletedUserDto.getId());
        assertEquals(user.getEmail(), deletedUserDto.getEmail());
        assertEquals(user.getFirstName(), deletedUserDto.getFirstName());
        assertEquals(user.getLastName(), deletedUserDto.getLastName());
        assertEquals(user.getBirthDate(), deletedUserDto.getBirthDate());
        assertEquals(user.getAddress(), deletedUserDto.getAddress());
        assertEquals(user.getPhoneNumber(), deletedUserDto.getPhoneNumber());

    }

}



