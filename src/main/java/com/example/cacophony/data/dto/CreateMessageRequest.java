package com.example.cacophony.data.dto;

import lombok.Data;
import lombok.Getter;

import java.util.UUID;

@Data
public class CreateMessageRequest {
    UUID userId;
    String conversationId;
    String message;
}
