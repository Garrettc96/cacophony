package com.example.cacophony.data.dto;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class GetMessageResponse {
    String id;
    String message;
    String userId;
    OffsetDateTime createdAt;
    OffsetDateTime updatedAt;
}
