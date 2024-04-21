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
    @Mapping(source = "rating",target = "rating")
    @Mapping(source = "release_year",target = "release_year")
    @Mapping(source = "length",target = "length")
    @Mapping(source = "genres",target = "genres")
    Movie movieDtoToMovie(MovieDTO movieDTO);

}
