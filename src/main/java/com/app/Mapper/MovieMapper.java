package com.app.Mapper;

import com.app.DTO.MovieDTO;
import com.app.Entity.Movie;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MovieMapper {

    MovieMapper INSTANCE = Mappers.getMapper(MovieMapper.class);

    @Mapping(source = "title",target = "title")
    @Mapping(source = "release_year",target = "release_year")
    @Mapping(source = "movie_length",target = "movie_length")
    @Mapping(source = "director_id",target = "director_id")
    @Mapping(source = "genre_id",target = "rating_id")
    Movie mapDtoToEntity(MovieDTO movieDTO);
}