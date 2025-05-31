package com.app.module.rating.mapper;

import com.app.module.rating.dto.RatingDTO;
import com.app.module.rating.entity.Rating;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RatingMapper {

    RatingMapper INSTANCE = Mappers.getMapper(RatingMapper.class);

    @Mapping(source = "rating",target = "rating")
    @Mapping(source = "movieId",target = "movieId")
    Rating mappingDtoToEntity(RatingDTO ratingDTO);

}
