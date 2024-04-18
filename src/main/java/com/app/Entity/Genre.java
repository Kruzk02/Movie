package com.app.Entity;

import org.springframework.data.annotation.Id;

public class Genre {

    @Id
    private Long id;
    private Movie movie;
    private GenreType genreType;

    public Genre() {
    }

    public Genre(Long id, Movie movie, GenreType genreType) {
        this.id = id;
        this.movie = movie;
        this.genreType = genreType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public GenreType getGenreType() {
        return genreType;
    }

    public void setGenreType(GenreType genreType) {
        this.genreType = genreType;
    }
}
