package com.app.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Set;

@Table("rating")
public class Rating {

    @Id
    private Long id;
    private RatingType ratingType;
    @Transient
    private Set<Movie> movies;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RatingType getRatingType() {
        return ratingType;
    }

    public void setRatingType(RatingType ratingType) {
        this.ratingType = ratingType;
    }

    public Set<Movie> getMovies() {
        return movies;
    }

    public void setMovies(Set<Movie> movies) {
        this.movies = movies;
    }
}

enum RatingType{
    ONE(1.0),
    TWO(2.0),
    THREE(3.0),
    FOUR(4.0),
    FIVE(5.0);

    RatingType(double v) {
    }
}
