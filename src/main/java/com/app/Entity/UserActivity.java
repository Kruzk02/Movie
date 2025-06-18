package com.app.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import module.movie.entity.Movie;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserActivity {

    private Long userId;
    private Movie movie; // Need to call api to get all actors, directors and genres

}
