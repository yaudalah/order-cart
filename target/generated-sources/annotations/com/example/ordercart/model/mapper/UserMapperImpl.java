package com.example.ordercart.model.mapper;

import com.example.ordercart.entity.User;
import com.example.ordercart.model.dto.UserDto;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-28T13:16:20+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Amazon.com Inc.)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User userDtoToUser(UserDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.username( userDto.getUsername() );
        user.email( userDto.getEmail() );
        user.phoneNumber( userDto.getPhoneNumber() );
        if ( userDto.getJoinDate() != null ) {
            user.joinDate( LocalDateTime.parse( userDto.getJoinDate() ) );
        }

        return user.build();
    }

    @Override
    public UserDto userToUserDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDto.UserDtoBuilder userDto = UserDto.builder();

        userDto.id( user.getId() );
        userDto.username( user.getUsername() );
        userDto.email( user.getEmail() );
        userDto.phoneNumber( user.getPhoneNumber() );
        if ( user.getJoinDate() != null ) {
            userDto.joinDate( DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( user.getJoinDate() ) );
        }

        return userDto.build();
    }
}
