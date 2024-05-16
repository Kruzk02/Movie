package com.app.Entity;

import org.springframework.data.relational.core.mapping.Table;

@Table("director_movie")
public class DirectorMoviePK {
    private Long director_id;
    private Long movie_id;

    public DirectorMoviePK() {
    }

    public DirectorMoviePK(Long director_id, Long movie_id) {
        this.director_id = director_id;
        this.movie_id = movie_id;
    }

    public Long getDirector_id() {
        return director_id;
    }

    public void setDirector_id(Long director_id) {
        this.director_id = director_id;
    }

    public Long getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(Long movie_id) {
        this.movie_id = movie_id;
    }
}
