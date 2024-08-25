package com.app.Mapper;

import com.app.DTO.ActorDTO;
import com.app.Entity.Actor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ActorMapper {

    ActorMapper INSTANCE = Mappers.getMapper(ActorMapper.class);

    @Mapping(source = "firstName",target = "firstName")
    @Mapping(source = "lastName",target = "lastName")
    @Mapping(source = "birthDate",target = "birthDate")
    @Mapping(source = "nationality",target = "nationality")
    @Mapping(source = "photo",target = "photo")
    Actor mapActorDtoToActor(ActorDTO actorDTO);
}
