package com.app.DTO;

import java.time.LocalTime;

public class MovieMediaDTO {

    private Long movieId;
    private byte episodes;
    private LocalTime duration;
    private String quality;
    private String video;

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public byte getEpisodes() {
        return episodes;
    }

    public void setEpisodes(byte episodes) {
        this.episodes = episodes;
    }

    public LocalTime  getDuration() {
        return duration;
    }

    public void setDuration(LocalTime duration) {
        this.duration = duration;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }
}
