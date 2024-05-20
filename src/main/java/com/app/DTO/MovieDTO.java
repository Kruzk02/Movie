package com.app.DTO;

import java.time.LocalDate;
import java.util.Set;

public class MovieDTO {

    private String title;
    private LocalDate release_year;
    private Long movie_length;
    private Set<Long> director_id;
    private Set<Long> genre_id;
    private Set<Long> actor_id;

    public String getTitle() {
        return title;
    }

    public LocalDate getRelease_year() {
        return release_year;
    }

    public Long getMovie_length() {
        return movie_length;
    }

    public Set<Long> getDirector_id() {
        return director_id;
    }

    public Set<Long> getGenre_id() {
        return genre_id;
    }

    public Set<Long> getActor_id() {
        return actor_id;
    }
}
