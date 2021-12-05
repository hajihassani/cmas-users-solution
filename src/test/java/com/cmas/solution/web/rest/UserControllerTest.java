package com.cmas.solution.web.rest;

import com.cmas.solution.dto.UserRequestDto;
import com.cmas.solution.dto.UserResponseDto;
import com.cmas.solution.service.UserService;
import com.cmas.solution.utils.exception.UserNotExistProblem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * Sample tests for 'controller' layer
 */
@AutoConfigureJsonTesters
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @Autowired
    private JacksonTester<UserRequestDto> jsonUserRequestDto;

    @Test
    public void createUserSuccess() throws Exception {

        // Given
        given(userService.createUser(any(UserRequestDto.class))).willAnswer(
                answer -> convertUserRequestDtoToResponseDto(answer.getArgument(0), 1L));

        // When
        final UserRequestDto userRequest = UserRequestDto.builder()
                .firstName("John").lastName("Doe").email("john@doe.com").age(50).active(true)
                .build();
        MockHttpServletResponse response = mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonUserRequestDto.write(userRequest).getJson())
        ).andReturn().getResponse();

        // Then
        String expectedContent = "{\"User ID\":1,\"First Name\":\"John\",\"Last Name\":\"Doe\"," +
                "\"Email\":\"john@doe.com\",\"Age\":50,\"Active\":true}";
        then(response.getStatus()).as("The response status must be OK.").isEqualTo(HttpStatus.OK.value());
        then(response.getContentAsString()).as("The response body must be as expected.")
                .isEqualTo(expectedContent);

    }

    @Test
    public void findUserThrowsNotFoundException() throws Exception {

        // Given
        given(userService.findUser(2L)).willThrow(new UserNotExistProblem(2L));

        // When
        MockHttpServletResponse response = mvc.perform(get("/users/2")).andReturn().getResponse();

        // Then
        then(response.getStatus()).as("The response status must be not found.")
                .isEqualTo(HttpStatus.NOT_FOUND.value());

    }

    @Test
    public void updateUserSuccess() throws Exception {

        // Given
        given(userService.updateUser(anyLong(), any(UserRequestDto.class))).willAnswer(
                answer -> convertUserRequestDtoToResponseDto(answer.getArgument(1), answer.getArgument(0)));

        // When
        final UserRequestDto userRequest = UserRequestDto.builder()
                .firstName("John").lastName("Doe").email("john@doe.com").age(50).active(true)
                .build();
        MockHttpServletResponse response = mvc.perform(put("/users/3")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonUserRequestDto.write(userRequest).getJson())
        ).andReturn().getResponse();

        // Then
        String expectedContent = "{\"User ID\":3,\"First Name\":\"John\",\"Last Name\":\"Doe\"," +
                "\"Email\":\"john@doe.com\",\"Age\":50,\"Active\":true}";
        then(response.getStatus()).as("The response status must be OK.").isEqualTo(HttpStatus.OK.value());
        then(response.getContentAsString()).as("The response body must be as expected.")
                .isEqualTo(expectedContent);

    }

    @Test
    public void deleteUserThrowsNotFoundException() throws Exception {

        // Given
        doAnswer(answer -> {
            throw new UserNotExistProblem(answer.getArgument(0));
        }).when(userService).deleteUser(anyLong());

        // When
        MockHttpServletResponse response = mvc.perform(delete("/users/4")).andReturn().getResponse();

        // Then
        then(response.getStatus()).as("The response status must be not found.")
                .isEqualTo(HttpStatus.NOT_FOUND.value());
        then(response.getContentLength()).as("The response body must be empty.").isZero();

    }

    private UserResponseDto convertUserRequestDtoToResponseDto(UserRequestDto requestDto, Long userId) {
        return UserResponseDto.builder()
                .userId(userId)
                .firstName(requestDto.getFirstName())
                .lastName(requestDto.getLastName())
                .email(requestDto.getEmail())
                .age(requestDto.getAge())
                .active(requestDto.getActive())
                .build();
    }

}
