package com.app.Entity;

import org.springframework.data.relational.core.mapping.Table;

@Table("actor_movie")
public class ActorMoviePK {
    private Long actor_id;
    private Long movie_id;

    public ActorMoviePK() {
    }

    public ActorMoviePK(Long actor_id, Long movie_id) {
        this.actor_id = actor_id;
        this.movie_id = movie_id;
    }

    public Long getActor_id() {
        return actor_id;
    }

    public void setActor_id(Long actor_id) {
        this.actor_id = actor_id;
    }

    public Long getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(Long movie_id) {
        this.movie_id = movie_id;
    }
}
