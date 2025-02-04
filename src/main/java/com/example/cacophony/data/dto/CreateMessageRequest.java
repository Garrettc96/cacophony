package com.example.cacophony.data.dto;

import com.example.cacophony.data.model.ModelType;
import com.example.cacophony.security.ResourceAccessType;
import lombok.Data;
import lombok.Getter;

import java.util.UUID;

@Data
public final class CreateMessageRequest implements ResourceAuthorizationBody {
    UUID userId;
    UUID conversationId;
    String message;

    @Override
    public String getAuthId() {
        return conversationId.toString();
    }

    @Override
    public ModelType getModelType() {
        return ModelType.MESSAGE;
    }
}
