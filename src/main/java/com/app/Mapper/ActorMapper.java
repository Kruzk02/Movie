package com.app.Mapper;

import com.app.DTO.ActorDTO;
import com.app.Entity.Actor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ActorMapper {
    ActorMapper INSTANCE = Mappers.getMapper(ActorMapper.class);

    @Mapping(source = "first_name",target = "first_name")
    @Mapping(source = "last_name",target = "last_name")
    @Mapping(source = "nationality",target = "nationality")
    @Mapping(source = "birthDate",target = "birthDate")
    Actor actorDtoToActor(ActorDTO actorDTO);
}
