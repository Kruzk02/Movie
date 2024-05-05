package com.app.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.util.Set;

@Table("movie")
public class Movie {
    @Id
    private Long id;
    @Column("title")
    private String title;
    @Column("release_year")
    private LocalDate release_year;
    @Column("length")
    private Long movie_length;

    @Column("director_id")
    private Long director_id;
    @Column("rating_id")
    private Long rating_id;
    @Column("genre_id")
    private Long genre_id;

    @Transient
    private Director director;
    @Transient
    private Set<Rating> ratings;
    @Transient
    private Set<Genre> genres;

    public Movie() {
    }

    public Movie(Long id, String title, LocalDate release_year, Long movie_length, Long director_id, Long rating_id, Long genre_id) {
        this.id = id;
        this.title = title;
        this.release_year = release_year;
        this.movie_length = movie_length;
        this.director_id = director_id;
        this.rating_id = rating_id;
        this.genre_id = genre_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Director getDirector() {
        return director;
    }

    public void setDirector(Director director) {
        this.director = director;
    }

    public Set<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(Set<Rating> ratings) {
        this.ratings = ratings;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }
}
