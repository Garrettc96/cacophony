package com.example.cacophony.data.dto;

import lombok.Builder;
import lombok.Data;

import java.rmi.server.UID;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
public class MessageResponse {
    String id;
    UUID userId;
    String conversationId;
    String message;
    OffsetDateTime createdAt;
    OffsetDateTime updatedAt;

}
