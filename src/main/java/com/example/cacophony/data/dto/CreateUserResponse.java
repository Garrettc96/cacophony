package com.example.cacophony.data.dto;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Builder
@Data
public class CreateUserResponse extends AbstractResponse {
    private UUID id;
    private String userName;
    private String email;
    private OffsetDateTime lastLogin;

}
