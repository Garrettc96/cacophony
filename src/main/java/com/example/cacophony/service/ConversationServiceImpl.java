package com.example.cacophony.service;

import java.util.UUID;

import com.example.cacophony.data.model.Conversation;
import com.example.cacophony.data.model.User;
import com.example.cacophony.exception.NotFoundException;
import com.example.cacophony.repository.ConversationRepository;

public class ConversationServiceImpl implements ConversationService {

    ConversationRepository conversationRepository;
    
    public ConversationServiceImpl(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    @Override
    public boolean isUserInConversation(UUID conversationId, UUID userId) {
        return this.conversationRepository.existsByIdAndMembersContains(conversationId, User.fromId(userId));
    }

    @Override
    public Conversation getConversation(String conversationId) {
        // TODO Auto-generated method stub
       return this.conversationRepository.findById(UUID.fromString(conversationId)).orElseThrow(
            () -> new NotFoundException(String.format("Conversation with id %s not found", conversationId))
        );
    }

}