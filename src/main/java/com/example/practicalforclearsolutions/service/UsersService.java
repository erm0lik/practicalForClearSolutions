package com.example.practicalforclearsolutions.service;

import com.example.practicalforclearsolutions.dto.Dates;
import com.example.practicalforclearsolutions.dto.UsersDto;
import com.example.practicalforclearsolutions.entity.Users;
import com.example.practicalforclearsolutions.repository.UsersRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.FeatureDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;

    @Transactional
    public UsersDto create(UsersDto createDto) {
        return entityToDto(usersRepository.save(dtoToEntity(createDto)));
    }

    @Transactional
    public UsersDto updateUsersFields(long id, UsersDto usersDto) {
        Users updateUser = usersRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("User with id " + id + " not found"));

        usersDto.setId(id);

        BeanUtils.copyProperties(usersDto, updateUser, getNullPropertyNames(usersDto));

        return entityToDto(updateUser);
    }

    @Transactional(readOnly = true)
    public List<UsersDto> findByBirthDateBetween(Dates dates) {
        List<Users> result = usersRepository.findByBirthDateBetween(dates.getFromDate(), dates.getToDate());
        if (result.isEmpty()) {
            throw new EntityNotFoundException("Users not found between the " + dates.getFromDate()
                    + " and " + dates.getToDate() + " dates.");
        } else {
            List<UsersDto> resultDto = new ArrayList<>(result.size());
            result.forEach(a -> resultDto.add(entityToDto(a)));
            return resultDto;
        }
    }

    @Transactional
    public UsersDto deleteUserById(long id) {
        Users deletedUser = usersRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("User with id " + id + " not found"));
        usersRepository.delete(deletedUser);
        return entityToDto(deletedUser);
    }

    private Users dtoToEntity(UsersDto usersDto) {
        return new ModelMapper().map(usersDto, Users.class);
    }

    private UsersDto entityToDto(Users users) {
        return new ModelMapper().map(users, UsersDto.class);
    }

    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        Set<String> emptyNames = Stream.of(src.getPropertyDescriptors())
                .filter(pd -> src.getPropertyValue(pd.getName()) == null)
                .map(FeatureDescriptor::getName)
                .collect(Collectors.toSet());
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
