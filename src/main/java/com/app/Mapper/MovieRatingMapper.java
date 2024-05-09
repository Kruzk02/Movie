package com.app.Mapper;

import com.app.DTO.MovieRatingDTO;
import com.app.Entity.MovieRatingPK;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MovieRatingMapper {

    MovieRatingMapper INSTANCE = Mappers.getMapper(MovieRatingMapper.class);

    @Mapping(source = "ratingId",target = "ratingId")
    @Mapping(source = "movieId",target = "movieId")
    MovieRatingPK mappingDtoToEntity(MovieRatingDTO movieRatingDTO);
}
