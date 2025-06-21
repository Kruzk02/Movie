package com.app.module.actor.dto;

import org.springframework.http.codec.multipart.FilePart;

import java.time.LocalDate;

public record ActorDTO (
    String firstName,
    String lastName,
    LocalDate birthDate,
    String nationality,
    FilePart photo
) {}