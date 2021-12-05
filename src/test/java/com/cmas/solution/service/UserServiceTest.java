package com.cmas.solution.service;

import com.cmas.solution.domain.UserEntity;
import com.cmas.solution.dto.UserRequestDto;
import com.cmas.solution.dto.UserResponseDto;
import com.cmas.solution.repository.UserRepository;
import com.cmas.solution.utils.exception.UserNotExistProblem;
import com.cmas.solution.utils.mapper.UserMapper;
import com.cmas.solution.utils.mapper.UserResponseAssembler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.*;

/**
 * Sample tests for 'service' layer
 */
@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserResponseAssembler userResponseAssembler;

    private UserService userService;

    @BeforeEach
    public void setup() {
        userService = new UserService(userRepository, userMapper, userResponseAssembler);
    }

    @Test
    public void createUserSuccess() {

        // Given
        given(userRepository.save(any(UserEntity.class)))
                .willAnswer(answer -> {
                    UserEntity savedUser = answer.getArgument(0);
                    savedUser.setId(ThreadLocalRandom.current().nextLong(1L, 1000L));
                    return savedUser;
                });

        // When
        final UserRequestDto userDto = UserRequestDto.builder()
                .firstName("John").lastName("Doe").email("john@doe.com").age(50).active(true)
                .build();
        final UserResponseDto createdUser = userService.createUser(userDto);

        // Then
        final UserResponseDto expectedUser = UserResponseDto.builder()
                .userId(null).firstName("John").lastName("Doe").email("john@doe.com").age(50).active(true)
                .build();
        then(createdUser.getUserId()).as("The user ID is set when stored.").isNotNull();
        createdUser.setUserId(null);
        then(createdUser).as("The created user have values as expected.").isEqualTo(expectedUser);

    }

    @Test
    public void findUserSuccess() {

        // Given
        final UserEntity userEntity = UserEntity.builder()
                .id(2L).firstName("John").lastName("Doe").email("john@doe.com").age(50).active(true)
                .build();
        given(userRepository.findById(anyLong())).willReturn(Optional.of(userEntity));

        // When
        final UserResponseDto foundUser = userService.findUser(anyLong());

        // Then
        final UserResponseDto expectedUser = UserResponseDto.builder()
                .userId(2L).firstName("John").lastName("Doe").email("john@doe.com").age(50).active(true)
                .build();
        then(foundUser).as("The found user have values as expected.").isEqualTo(expectedUser);

    }

    @Test
    public void updateUserThrowsNotFoundException() {

        // Given
        given(userRepository.findById(anyLong())).willReturn(Optional.empty());

        // When
        final Throwable throwable = catchThrowable(() -> userService.updateUser(3L, UserRequestDto.builder().build()));

        // Then
        then(throwable)
                .as("For the empty optional the update method should throw a not found exception.")
                .isInstanceOf(UserNotExistProblem.class)
                .as("The exception message should contain the user ID.")
                .hasMessageContaining("User '3'");

    }

    @Test
    public void deleteUserThrowsNotFoundException() {

        // Given
        doThrow(EmptyResultDataAccessException.class).when(userRepository).deleteById(anyLong());

        // When
        final Throwable throwable = catchThrowable(() -> userService.deleteUser(4L));

        // Then
        then(throwable)
                .as("If the user ID does not exist, delete method should throw a not found exception.")
                .isInstanceOf(UserNotExistProblem.class)
                .as("The exception message should contain the user ID.")
                .hasMessageContaining("User '4'");

    }

}
