package com.app.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.sql.Timestamp;

@Table("movie_media")
public class MovieMedia implements Serializable {

    @Id
    private Long id;
    @Column("movie_id")
    private Long movieId;
    @Column("file_path")
    private String filePath;
    @Column("episodes")
    private byte episodes;
    @Column("duration")
    private Timestamp duration;
    @Column("quality")
    private String quality;

    public MovieMedia() {
    }

    public MovieMedia(Long id, Long movieId, String filePath, byte episodes, Timestamp duration, String quality) {
        this.id = id;
        this.movieId = movieId;
        this.filePath = filePath;
        this.episodes = episodes;
        this.duration = duration;
        this.quality = quality;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public byte getEpisodes() {
        return episodes;
    }

    public void setEpisodes(byte episodes) {
        this.episodes = episodes;
    }

    public Timestamp getDuration() {
        return duration;
    }

    public void setDuration(Timestamp duration) {
        this.duration = duration;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    @Override
    public String toString() {
        return "MovieMedia{" +
                "id=" + id +
                ", movieId=" + movieId +
                ", filePath='" + filePath + '\'' +
                ", episodes=" + episodes +
                ", duration=" + duration +
                ", quality='" + quality + '\'' +
                '}';
    }
}
