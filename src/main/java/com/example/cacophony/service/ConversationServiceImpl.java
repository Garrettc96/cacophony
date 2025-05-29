package com.example.cacophony.service;

import java.util.List;
import java.util.UUID;

import com.example.cacophony.data.model.Conversation;
import com.example.cacophony.data.model.ConversationWithMembers;
import com.example.cacophony.data.model.User;
import com.example.cacophony.exception.NotFoundException;
import com.example.cacophony.jooq.tables.records.ConversationRecord;
import com.example.cacophony.repository.ConversationJooqRepository;
import com.example.cacophony.repository.ConversationRepository;

public class ConversationServiceImpl implements ConversationService {

    ConversationJooqRepository conversationRepository;

    public ConversationServiceImpl(ConversationJooqRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    @Override
    public boolean isUserInConversation(UUID conversationId, UUID userId) {
        return this.conversationRepository.existsByIdAndMembersContains(conversationId, userId);
    }

    @Override
    public ConversationRecord getConversation(String conversationId) {
        return this.conversationRepository.findById(UUID.fromString(conversationId)).orElseThrow(
                () -> new NotFoundException(String.format("Conversation with id %s not found", conversationId)));
    }

    @Override
    public ConversationRecord createConversation(ConversationRecord conversation, List<UUID> members) {
        ConversationRecord createdConversation = this.conversationRepository.createConversation(conversation);
        this.conversationRepository.addUsersToConversation(members, createdConversation.getId());
        return createdConversation;
    }

    @Override
    public ConversationWithMembers getConversationWithMembers(UUID conversationId) {
        return this.conversationRepository.getConversationWithMembers(conversationId);
    }

}
