package com.app.Entity;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("Movie_Rating")
public class MovieRatingPK {

    @Column("movie_id")
    private Long movieId;
    @Column("rating_id")
    private Long ratingId;

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public Long getRatingId() {
        return ratingId;
    }

    public void setRatingId(Long ratingId) {
        this.ratingId = ratingId;
    }

    @Override
    public String toString() {
        return "MovieRatingPK{" +
                "movieId=" + movieId +
                ", ratingId=" + ratingId +
                '}';
    }
}
