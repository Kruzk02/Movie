package com.app.module.director.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.codec.multipart.FilePart;

import java.time.LocalDate;

public record DirectorDTO (
    String firstName,
    String lastName,
    LocalDate birthDate,
    String nationality,
    FilePart photo
) {}
