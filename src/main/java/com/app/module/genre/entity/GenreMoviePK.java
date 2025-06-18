package com.app.module.genre.entity;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("Genre_Movie")
public class GenreMoviePK {

    @Column("genre_id")
    private Long genre_id;
    @Column("movie_id")
    private Long movie_id;

    public GenreMoviePK() {
    }

    public GenreMoviePK(Long genre_id, Long movie_id) {
        this.genre_id = genre_id;
        this.movie_id = movie_id;
    }

    public Long getGenre_id() {
        return genre_id;
    }

    public void setGenre_id(Long genre_id) {
        this.genre_id = genre_id;
    }

    public Long getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(Long movie_id) {
        this.movie_id = movie_id;
    }
}
