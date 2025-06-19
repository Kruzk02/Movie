package com.app.module.user.dto;

import com.app.type.Nationality;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public record UserDTO (
        String username,
        String email,
        String password,
        String phoneNumber,
        Nationality nationality,
        LocalDate birthDate
) {}