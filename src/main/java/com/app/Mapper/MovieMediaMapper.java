package com.app.Mapper;

import com.app.DTO.MovieMediaDTO;
import com.app.Entity.MovieMedia;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MovieMediaMapper {
    MovieMediaMapper INSTANCE = Mappers.getMapper(MovieMediaMapper.class);

    @Mapping(source = "movieId",target = "movieId")
    @Mapping(source = "episodes",target = "episodes")
    @Mapping(source = "duration",target = "duration")
    @Mapping(source = "quality",target = "quality")
    @Mapping(source = "video",target = "filePath")
    MovieMedia mapDtoToEntity(MovieMediaDTO movieMediaDTO);
}
