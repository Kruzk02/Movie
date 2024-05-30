package com.app.DTO;

import java.io.Serializable;
import java.time.LocalDate;

public class MovieDTO implements Serializable {

    private String title;
    private LocalDate release_year;
    private Long movie_length;

    public String getTitle() {
        return title;
    }

    public LocalDate getRelease_year() {
        return release_year;
    }

    public Long getMovie_length() {
        return movie_length;
    }
}
