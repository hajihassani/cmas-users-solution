package com.cmas.solution.utils.mapper;

import com.cmas.solution.domain.UserEntity;
import com.cmas.solution.dto.UserResponseDto;
import com.cmas.solution.web.rest.UserController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * The 'UserResponseAssembler' class consists of methods which help to add HATEOAS features
 * to the response objects
 */
@Component
public class UserResponseAssembler extends RepresentationModelAssemblerSupport<UserEntity, UserResponseDto> {

    private final UserMapper userMapper;
    private final PagedResourcesAssembler<UserEntity> pagedResourcesAssembler;

    public UserResponseAssembler(UserMapper userMapper, PagedResourcesAssembler<UserEntity> pagedResourcesAssembler) {
        super(UserController.class, UserResponseDto.class);
        this.userMapper = userMapper;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @Override
    public UserResponseDto toModel(UserEntity entity) {
        UserResponseDto userDto = userMapper.userEntityToResponseDto(entity);
        userDto.add(
                linkTo(methodOn(UserController.class).findUser(entity.getId())).withSelfRel()
                        .andAffordance(afford(methodOn(UserController.class).updateUser(entity.getId(), null)))
                        .andAffordance(afford(methodOn(UserController.class).deleteUser(entity.getId()))),
                linkTo(methodOn(UserController.class).findAllUsers(Pageable.unpaged())).withRel("all-users")
        );
        return userDto;
    }

    public PagedModel<UserResponseDto> toPagedModel(Page<UserEntity> entities) {
        return pagedResourcesAssembler.toModel(entities, this);
    }

}
