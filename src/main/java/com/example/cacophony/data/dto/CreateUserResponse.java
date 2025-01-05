package com.example.cacophony.data.dto;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Builder
@Data
public class CreateUserResponse extends AbstractResponse {
    private String id;
    private String userName;
    private String email;
    private OffsetDateTime lastLogin;

}
