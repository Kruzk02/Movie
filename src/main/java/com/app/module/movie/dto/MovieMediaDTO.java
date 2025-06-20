package com.app.module.movie.dto;

import org.springframework.http.codec.multipart.FilePart;

import java.time.LocalTime;

public record MovieMediaDTO (
    Long movieId,
    byte episodes,
    LocalTime duration,
    String quality,
    FilePart video
) {}
