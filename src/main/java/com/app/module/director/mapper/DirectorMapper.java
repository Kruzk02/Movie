package com.app.module.director.mapper;

import com.app.module.director.dto.DirectorDTO;
import com.app.module.director.entity.Director;
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
