package com.app.Mapper;

import com.app.DTO.MovieDTO;
import com.app.Entity.Director;
import com.app.Entity.Genre;
import com.app.Entity.Movie;
import com.app.Entity.Nationality;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

class MovieMapperTest {
    public static void main(String[] args){
        Set<Genre> genres = new HashSet<>();
        genres.add(Genre.Action);

        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setTitle("title");
        movieDTO.setRating("ONE");
        movieDTO.setGenres(genres);
        movieDTO.setDirector(new Director(1L,"name","name", Nationality.VIETNAM,new Date()));
        movieDTO.setRelease_year(new Date(2002));
        movieDTO.setLength(120);

        Movie movie = MovieMapper.INSTANCE.movieDtoToMovie(movieDTO);

        System.out.println(movie.toString());
    }
}