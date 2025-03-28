package com.example.cacophony.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

import com.example.cacophony.data.model.User;

@Builder
@Data
@AllArgsConstructor
public class CreateUserResponse {
    private UUID id;
    private String userName;
    private String email;
    private OffsetDateTime lastLogin;

    public static CreateUserResponse fromUser(User user) {
        return new CreateUserResponse(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            null
        );
    }
}
