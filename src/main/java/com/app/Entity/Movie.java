package com.app.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Movie {

    @Id
    private Long id;
    private String title;
    private Rating rating;
    private Date release_year;
    private Integer length;
    private Director director;
    private Set<Genre> genres;

}
