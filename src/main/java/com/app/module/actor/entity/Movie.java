package com.app.module.actor.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class Movie {
    @Id
    private Long id;
    @Column("title")
    private String title;
    @Column("release_year")
    private LocalDate releaseYear;
    @Column("seasons")
    private byte seasons;
    @Column("poster")
    private String poster;
}
