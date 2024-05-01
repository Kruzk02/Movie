package com.app.DTO;

import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDate;

public class MovieDTO {

    private String title;
    private LocalDate release_year;
    private Long movie_length;
    private Long director_id;
    private Long rating_id;
    private Long genre_id;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getRelease_year() {
        return release_year;
    }

    public void setRelease_year(LocalDate release_year) {
        this.release_year = release_year;
    }

    public Long getMovie_length() {
        return movie_length;
    }

    public void setMovie_length(Long movie_length) {
        this.movie_length = movie_length;
    }

    public Long getDirector_id() {
        return director_id;
    }

    public void setDirector_id(Long director_id) {
        this.director_id = director_id;
    }

    public Long getRating_id() {
        return rating_id;
    }

    public void setRating_id(Long rating_id) {
        this.rating_id = rating_id;
    }

    public Long getGenre_id() {
        return genre_id;
    }

    public void setGenre_id(Long genre_id) {
        this.genre_id = genre_id;
    }
}
