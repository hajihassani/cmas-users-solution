package com.cmas.solution.service;

import com.cmas.solution.domain.UserEntity;
import com.cmas.solution.dto.UserRequestDto;
import com.cmas.solution.dto.UserResponseDto;
import com.cmas.solution.repository.UserRepository;
import com.cmas.solution.utils.exception.UserNotExistProblem;
import com.cmas.solution.utils.mapper.UserMapper;
import com.cmas.solution.utils.mapper.UserResponseAssembler;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

/**
 * The 'UserService' class consists of the methods to handle CRUD operations on user objects in database
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserResponseAssembler userResponseAssembler;

    public UserService(UserRepository userRepository, UserMapper userMapper, UserResponseAssembler userResponseAssembler) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userResponseAssembler = userResponseAssembler;
    }

    /**
     * The method creates a new user by input values
     *
     * @param user consists of new user parameters such as 'first name', 'last name', 'email', ...
     * @return returns the user object saved in database
     */
    public UserResponseDto createUser(UserRequestDto user) {
        return userResponseAssembler.toModel(userRepository.save(userMapper.userRequestDtoToEntity(user)));
    }

    /**
     * The method finds if the user exists in database currently
     * and in case of existence will return the corresponding object
     *
     * @param userId is the database ID of the user
     * @return returns the user object found in database
     */
    public UserResponseDto findUser(Long userId) {
        return userRepository.findById(userId).map(userResponseAssembler::toModel)
                .orElseThrow(() -> new UserNotExistProblem(userId));
    }

    /**
     * The method lists all the users currently exist in database with pagination
     *
     * @param pageable consists of the page number and the size of each page as query parameters
     * @return returns the list of user objects exist in database
     */
    public PagedModel<UserResponseDto> findAllUsers(Pageable pageable) {
        return userResponseAssembler.toPagedModel(userRepository.findAll(pageable));
    }

    /**
     * The method updates the parameters of an existing user in database
     *
     * @param userId is the database ID of the user
     * @param user   consists of the new value to edit parameters of the current object
     * @return returns the updated user object in database
     */
    public UserResponseDto updateUser(Long userId, UserRequestDto user) {
        return userRepository.findById(userId).map((foundUser) -> {
            UserEntity userEntity = userMapper.userRequestDtoToEntity(user);
            userEntity.setId(userId);
            return userResponseAssembler.toModel(userRepository.save(userEntity));
        }).orElseThrow(() -> new UserNotExistProblem(userId));
    }

    /**
     * The method removes an existing user from database
     *
     * @param userId is the database ID of the user
     */
    public void deleteUser(Long userId) {
        try {
            userRepository.deleteById(userId);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotExistProblem(userId);
        }
    }

}
