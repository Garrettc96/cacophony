package com.example.cacophony.data.dto;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
public class GetMessageResponse {
    UUID id;
    String message;
    String userId;
    OffsetDateTime createdAt;
    OffsetDateTime updatedAt;
}
