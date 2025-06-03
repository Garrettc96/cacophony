package com.example.cacophony.data.dto;

import com.example.cacophony.data.model.ModelType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnoreType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public final class CreateMessageRequest implements ResourceAuthorizationBody {

    UUID conversationId;
    String message;
    Optional<String> s3Path;

    @Override
    @JsonIgnore
    public String getAuthId() {
        return conversationId.toString();
    }

    @Override
    @JsonIgnore
    public ModelType getModelType() {
        return ModelType.MESSAGE;
    }
}
