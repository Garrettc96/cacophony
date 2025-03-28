package com.example.cacophony.data.dto;

import com.example.cacophony.data.model.ModelType;

public sealed interface ResourceAuthorizationBody permits CreateMessageRequest {
    public String getAuthId();

    public ModelType getModelType();
}
