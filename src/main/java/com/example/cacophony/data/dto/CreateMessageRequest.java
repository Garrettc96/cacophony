package com.example.cacophony.data.dto;

import lombok.Data;
import lombok.Getter;

@Data
public class CreateMessageRequest {
    String userId;
    String conversationId;
    String message;
}