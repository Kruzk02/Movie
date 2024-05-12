package com.app.Mapper;

import com.app.DTO.GenreMovieDTO;
import com.app.Entity.Genre;
import com.app.Entity.GenreMoviePK;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GenreMovieMapper {

    GenreMovieMapper INSTANCE = Mappers.getMapper(GenreMovieMapper.class);

    @Mapping(source = "genreId", target = "genre_id")
    @Mapping(source = "movieId", target = "movie_id")
    GenreMoviePK mappingDtoToEntity(GenreMovieDTO genreMovieDTO);
}
