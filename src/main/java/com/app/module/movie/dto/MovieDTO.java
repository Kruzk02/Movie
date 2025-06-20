package com.app.module.movie.dto;
import org.springframework.http.codec.multipart.FilePart;

import java.time.LocalDate;

public record MovieDTO (
    String title,
    LocalDate releaseYear,
    String description,
    byte seasons,
    FilePart poster
) {}

