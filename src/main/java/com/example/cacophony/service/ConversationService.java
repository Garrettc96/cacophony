package com.example.cacophony.service;

import java.util.List;
import java.util.UUID;

import com.example.cacophony.data.model.ConversationWithMembers;
import com.example.cacophony.jooq.tables.records.ConversationRecord;

public interface ConversationService {
    public boolean isUserInConversation(UUID conversationId, UUID userId);

    public ConversationRecord getConversation(String conversationId);

    public ConversationRecord createConversation(ConversationRecord conversation, List<UUID> members);

    public ConversationWithMembers getConversationWithMembers(UUID conversationId);
}
