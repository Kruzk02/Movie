package com.app.Entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Set;

@Table("genre")
public class Genre {

    @Id
    private Long id;
    private GenreType genreType;
    @Transient
    private Set<Movie> movies;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GenreType getGenreType() {
        return genreType;
    }

    public void setGenreType(GenreType genreType) {
        this.genreType = genreType;
    }

    public Set<Movie> getMovies() {
        return movies;
    }

    public void setMovies(Set<Movie> movies) {
        this.movies = movies;
    }
}

enum GenreType {
    Action,
    Adventure,
    Comedy,
    Drama,
    Romance,
    Horror,
    Science_Fiction,
    Fantasy,
    Thriller,
    Mystery,
    Crime,
    Animation,
    Documentary,
    Historical,
    Biographical,
    Musical,
    War,
    Western,
    Spy_Espionage,
    Supernatural,
    Psychological,
    Family,
    Sports,
    Disaster,
    Romance_Comedy,
    Musical_Comedy,
    Martial_Arts,
    Noir,
    Satire,
    Mockumentary
}
