package com.app.Mapper;

import com.app.DTO.DirectorDTO;
import com.app.Entity.Director;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DirectorMapper {
    DirectorMapper INSTANCE = Mappers.getMapper(DirectorMapper.class);

    @Mapping(source = "first_name",target = "first_name")
    @Mapping(source = "last_name",target = "last_name")
    @Mapping(source = "nationality",target = "nationality")
    @Mapping(source = "birthDate",target = "birthDate")
    Director directorDtoToDirector(DirectorDTO directorDTO);
}
