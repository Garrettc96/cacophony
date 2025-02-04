package com.example.cacophony.controllers;
import com.example.cacophony.data.UserRole;
import com.example.cacophony.data.dto.CreateUserRequest;
import com.example.cacophony.data.dto.CreateUserResponse;
import com.example.cacophony.data.model.AuthRequest;
import com.example.cacophony.data.model.User;
import com.example.cacophony.mapper.ModelMapper;
import com.example.cacophony.service.JwtService;
import com.example.cacophony.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public UserController(UserService userService, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(
            @Parameter(description = "UUID of user being requested") @Valid @PathVariable UUID id) {
        return ResponseEntity.ok(this.userService.getUserFromId(id));
    }

    @GetMapping()
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(this.userService.listUsers());
    }

    @PostMapping("/addNewUser")
    public String addNewUser(@RequestBody User user) {
        return this.userService.createUser(user).getId().toString();
    }

    @GetMapping("/userProfile")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String userProfile() {
        return "Welcome to User Profile";
    }

    @PostMapping("/generateToken")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
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
