package com.app.DTO;

import java.io.Serializable;
import java.time.LocalDate;

public class MovieDTO implements Serializable {

    private String title;
    private LocalDate release_year;
    private String description;
    private byte seasons;
    private String poster;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getRelease_year() {
        return release_year;
    }

    public void setRelease_year(LocalDate release_year) {
        this.release_year = release_year;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte getSeasons() {
        return seasons;
    }

    public void setSeasons(byte seasons) {
        this.seasons = seasons;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
}
