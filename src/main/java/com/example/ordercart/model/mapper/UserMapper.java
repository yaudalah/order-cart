package com.example.ordercart.model.mapper;

import com.example.ordercart.entity.User;
import com.example.ordercart.model.dto.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User userDtoToUser(UserDto userDto);
    UserDto userToUserDto(User user);
}
