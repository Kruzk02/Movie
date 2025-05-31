package com.app.module.movie.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieDTO {
    private String title;
    private LocalDate releaseYear;
    private String description;
    private byte seasons;
    private String poster;
}

