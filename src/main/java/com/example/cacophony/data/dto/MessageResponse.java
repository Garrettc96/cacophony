package com.example.cacophony.data.dto;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class MessageResponse {
    String id;
    String userId;
    String conversationId;
    String message;
    OffsetDateTime createdAt;
    OffsetDateTime updatedAt;

}
