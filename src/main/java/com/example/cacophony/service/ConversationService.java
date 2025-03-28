package com.example.cacophony.service;

import java.util.UUID;

import com.example.cacophony.data.model.Conversation;

public interface ConversationService {
    public boolean isUserInConversation(UUID conversationId, UUID userId);
    public Conversation getConversation(String conversationId);

}