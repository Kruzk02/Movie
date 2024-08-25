package com.app.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class AppConfig {

    @Bean
    public boolean createMoviePosterDirectory() {
        File file = new File("moviePoster");
        if (!file.exists()) {
            return file.mkdirs();
        }
        return file.exists();
    }

    @Bean
    public boolean createDirectorPhotoDirectory() {
        File file = new File("directorPhoto");
        if (!file.exists()) {
            return file.mkdirs();
        }
        return file.exists();
    }

    @Bean
    public boolean createActorPhotoDirectory() {
        File file = new File("actorPhoto");
        if (!file.exists()) {
            return file.mkdirs();
        }
        return file.exists();
    }

    @Bean
    public boolean createMovieMediaDirectory() {
        File file = new File("movieMedia");
        if (!file.exists()) {
            return file.mkdirs();
        }
        return file.exists();
    }
}
