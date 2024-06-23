package com.app.Mapper;

import com.app.DTO.UserDTO;
import com.app.Entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "username",target = "username")
    @Mapping(source = "email",target = "email")
    @Mapping(source = "phoneNumber",target = "phoneNumber")
    @Mapping(source = "nationality",target = "nationality")
    @Mapping(source = "birthDate",target = "birthDate")
    User mapDTOToEntity(UserDTO userDTO);
}
