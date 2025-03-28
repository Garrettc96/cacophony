package com.example.cacophony.controllers;

import com.example.cacophony.data.UserRole;
import com.example.cacophony.data.dto.CreateUserRequest;
import com.example.cacophony.data.dto.CreateUserResponse;
import com.example.cacophony.data.dto.GenerateTokenResponse;
import com.example.cacophony.data.model.AuthRequest;
import com.example.cacophony.data.model.User;
import com.example.cacophony.mapper.ModelMapper;
import com.example.cacophony.service.JwtService;
import com.example.cacophony.service.UserService;
import com.example.cacophony.util.Identity;
import com.example.cacophony.util.Jwt;

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

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper modelMapper;

    public UserController(UserService userService, JwtService jwtService, AuthenticationManager authenticationManager,
            ModelMapper modelMapper) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.modelMapper = modelMapper;
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
    public ResponseEntity<CreateUserResponse> addNewUser(@RequestBody User user) {
        return ResponseEntity.ok(CreateUserResponse.fromUser(this.userService.createUser(user)));
    }

    @GetMapping("/userProfile")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String userProfile() {
        return "Welcome to User Profile";
    }

    @PostMapping("/generateToken")
    public ResponseEntity<GenerateTokenResponse> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return ResponseEntity
                    .ok(GenerateTokenResponse.builder().token(jwtService.generateToken(authRequest.getUsername()))
                            .userId(userService.getUserFromName(authRequest.getUsername()).getId())
                            .createdAt(Jwt.getIssuedAt()).validUntil(Jwt.getExpiration()).build());

        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CreateUserResponse> createUser(@RequestBody CreateUserRequest createRequest) {
        return ResponseEntity
                .ok(modelMapper.userToResponse(this.userService.createUser(modelMapper.requestToUser(createRequest))));
    }
}
