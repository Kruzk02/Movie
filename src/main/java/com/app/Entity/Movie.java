package com.app.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Table("movie")
public class Movie implements Serializable {
    @Id
    private Long id;
    @Column("title")
    private String title;
    @Column("release_year")
    private LocalDate releaseYear;
    @Column("length")
    private Long movieLength;

    @Transient
    private Set<Director> directors;
    @Transient
    private Set<Rating> ratings;
    @Transient
    private Set<Genre> genres;
    @Transient
    private Set<Actor> actors;

    public Movie() {
    }

    public Movie(Long id, String title, LocalDate releaseYear, Long movieLength, Set<Director> directors, Set<Rating> ratings, Set<Genre> genres, Set<Actor> actors) {
        this.id = id;
        this.title = title;
        this.releaseYear = releaseYear;
        this.movieLength = movieLength;
        this.directors = directors;
        this.ratings = ratings;
        this.genres = genres;
        this.actors = actors;
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

    public LocalDate getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(LocalDate releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Long getMovieLength() {
        return movieLength;
    }

    public void setMovieLength(Long movieLength) {
        this.movieLength = movieLength;
    }

    public Set<Director> getDirectors() {
        return directors;
    }

    public void setDirectors(Set<Director> directors) {
        this.directors = directors;
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

    public Set<Actor> getActors() {
        return actors;
    }

    public void setActors(Set<Actor> actors) {
        this.actors = actors;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", releaseYear=" + releaseYear +
                ", movieLength=" + movieLength +
                ", directors=" + directors +
                ", ratings=" + ratings +
                ", genres=" + genres +
                ", actors=" + actors +
                '}';
    }
}
