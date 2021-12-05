package com.cmas.solution.web.rest;

import com.cmas.solution.dto.UserRequestDto;
import com.cmas.solution.dto.UserResponseDto;
import com.cmas.solution.service.UserService;
import com.sun.istack.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * The 'UserController' contains the REST endpoints for CRUD operation on user objects
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@NotNull @RequestBody UserRequestDto user) {
        return new ResponseEntity<>(userService.createUser(user), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findUser(@PathVariable("id") Long userId) {
        return new ResponseEntity<>(userService.findUser(userId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PagedModel<UserResponseDto>> findAllUsers(Pageable pageable) {
        return new ResponseEntity<>(userService.findAllUsers(pageable), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable("id") Long userId, @NotNull @RequestBody UserRequestDto user) {
        return new ResponseEntity<>(userService.updateUser(userId, user), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

}
