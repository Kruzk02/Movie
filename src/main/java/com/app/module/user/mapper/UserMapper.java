package com.app.module.user.mapper;

import com.app.module.user.dto.UserDTO;
import com.app.module.user.entity.User;
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
