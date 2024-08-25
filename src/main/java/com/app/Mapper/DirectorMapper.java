package com.app.Mapper;

import com.app.DTO.DirectorDTO;
import com.app.Entity.Director;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DirectorMapper {

    DirectorMapper INSTANCE = Mappers.getMapper(DirectorMapper.class);

    @Mapping(source = "firstName",target = "firstName")
    @Mapping(source = "lastName",target = "lastName")
    @Mapping(source = "birthDate",target = "birthDate")
    @Mapping(source = "nationality",target = "nationality")
    @Mapping(source = "photo",target = "photo")
    Director mapDirectorDtoToDirector(DirectorDTO directorDTO);
}
