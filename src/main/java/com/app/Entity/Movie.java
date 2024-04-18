package com.app.Entity;

import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.Set;

public class Movie {

    @Id
    private Long id;
    private String title;
    private Rating rating;
    private Date release_year;
    private Integer length;
    private Director director;
    private Set<Genre> genres;

    public Movie() {
    }

    public Movie(Long id, String title, Rating rating, Date release_year, Integer length, Director director, Set<Genre> genres) {
        this.id = id;
        this.title = title;
        this.rating = rating;
        this.release_year = release_year;
        this.length = length;
        this.director = director;
        this.genres = genres;
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

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public Date getRelease_year() {
        return release_year;
    }

    public void setRelease_year(Date release_year) {
        this.release_year = release_year;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Director getDirector() {
        return director;
    }

    public void setDirector(Director director) {
        this.director = director;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }
}
