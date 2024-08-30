package com.app.Entity;

import java.util.Objects;

public class UserActivity {

    private Long userId;
    private Movie movie;

    public UserActivity() {
    }

    public UserActivity(Long userId, Movie movie) {
        this.userId = userId;
        this.movie = movie;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserActivity that = (UserActivity) o;
        return Objects.equals(userId, that.userId) && Objects.equals(movie, that.movie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, movie);
    }

    @Override
    public String toString() {
        return "UserActivity{" +
                "userId=" + userId +
                ", movie=" + movie +
                '}';
    }
}
