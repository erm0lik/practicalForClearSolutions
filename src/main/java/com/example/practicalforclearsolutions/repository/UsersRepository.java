package com.example.practicalforclearsolutions.repository;

import com.example.practicalforclearsolutions.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface UsersRepository extends JpaRepository<Users, Long> {
    List<Users> findByBirthDateBetween(LocalDate startDate, LocalDate endDate);
}
