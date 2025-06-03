package com.example.cacophony.data.dto;

import com.example.cacophony.data.model.User;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ChatResponse {
    UUID id;
    String name;
    String description;
    OffsetDateTime createdAt;
    OffsetDateTime updatedAt;
    List<UUID> members;
}
