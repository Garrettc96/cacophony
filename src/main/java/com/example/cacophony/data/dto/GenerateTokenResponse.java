package com.example.cacophony.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor

public class GenerateTokenResponse {
    UUID userId;
    String token;
    Date createdAt;
    Date validUntil;
}
