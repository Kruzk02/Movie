package com.app.Mapper;

import com.app.DTO.RatingDTO;
import com.app.Entity.Rating;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RatingMapper {

    RatingMapper INSTANCE = Mappers.getMapper(RatingMapper.class);

    @Mapping(source = "rating",target = "rating")
    @Mapping(source = "movie",target = "movie")
    Rating mappingDtoToEntity(RatingDTO ratingDTO);

}
