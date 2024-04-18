package com.app.Entity;

public class Rating {

    private Long id;
    private RatingType ratingType;

    public Rating() {
    }

    public Rating(Long id, RatingType ratingType) {
        this.id = id;
        this.ratingType = ratingType;
    }

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
}
