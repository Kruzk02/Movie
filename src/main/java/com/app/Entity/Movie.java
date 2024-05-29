package com.app.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.time.LocalDate;

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

    public Movie() {
    }

    public Movie(Long id, String title, LocalDate releaseYear, Long movieLength) {
        this.id = id;
        this.title = title;
        this.releaseYear = releaseYear;
        this.movieLength = movieLength;
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

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", releaseYear=" + releaseYear +
                ", movieLength=" + movieLength +
                '}';
    }
}
