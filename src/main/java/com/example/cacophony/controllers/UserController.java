package com.example.cacophony.controllers;
import com.example.cacophony.data.dto.CreateUserRequest;
import com.example.cacophony.data.dto.CreateUserResponse;
import com.example.cacophony.data.model.User;
import com.example.cacophony.mapper.ModelMapper;
import com.example.cacophony.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(
            @Parameter(description = "UUID of user being requested") @Valid @PathVariable String id) {
        return ResponseEntity.ok(this.userService.getUserFromId(id));
    }

    @GetMapping()
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(this.userService.listUsers());
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public CreateUserResponse createUser(@RequestBody CreateUserRequest createRequest) {
        return ModelMapper.userToResponse(
                this.userService.createUser(
                        ModelMapper.requestToUser(createRequest)
                )
        );
    }
}
