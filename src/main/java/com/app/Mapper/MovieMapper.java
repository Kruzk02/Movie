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
    @Mapping(source = "release_year",target = "releaseYear")
    @Mapping(source = "description",target = "description")
    @Mapping(source = "seasons",target = "seasons")
    Movie mapDtoToEntity(MovieDTO movieDTO);
}
