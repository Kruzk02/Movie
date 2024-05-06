package com.app.DTO;

import com.app.Entity.Movie;

public class RatingDTO {

    private Double rating;
    private Movie movie;

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}
