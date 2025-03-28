package com.example.cacophony.data.dto;

import com.example.cacophony.data.model.ModelType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public final class CreateMessageRequest implements ResourceAuthorizationBody {

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
