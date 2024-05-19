package com.app.Mapper;

import com.app.DTO.RatingDTO;
import com.app.Entity.Rating;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RatingMapper {

    RatingMapper INSTANCE = Mappers.getMapper(RatingMapper.class);

    @Mapping(source = "ratingId",target = "id")
    Rating mappingDtoToEntity(RatingDTO ratingDTO);

}
