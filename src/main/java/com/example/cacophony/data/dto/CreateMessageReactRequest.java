package com.example.cacophony.data.dto;

import java.util.UUID;

public class CreateMessageReactRequest {
    private UUID messageId;
    private UUID reactId;

    public UUID getMessageId() {
        return messageId;
    }

    public void setMessageId(UUID messageId) {
        this.messageId = messageId;
    }

    public UUID getReactId() {
        return reactId;
    }

    public void setReactId(UUID reactId) {
        this.reactId = reactId;
    }
}
