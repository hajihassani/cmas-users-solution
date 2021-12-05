package com.cmas.solution.utils.mapper;

import com.cmas.solution.domain.UserEntity;
import com.cmas.solution.dto.UserRequestDto;
import com.cmas.solution.dto.UserResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

/**
 * The 'UserMapper' class contains methods that are necessary
 * to convert 'user DTOs' and 'user DB entity' to each other
 */
@Component
@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "id", target = "userId")
    UserResponseDto userEntityToResponseDto(UserEntity userEntity);

    @Mapping(target = "id", ignore = true)
    UserEntity userRequestDtoToEntity(UserRequestDto userDto);

}
