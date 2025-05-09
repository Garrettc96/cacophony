package com.example.cacophony.service;

import com.example.cacophony.data.model.Message;
import com.example.cacophony.data.model.User;
import com.example.cacophony.exception.NotFoundException;
import com.example.cacophony.repository.MessageRepository;

import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public class MessageServiceImpl implements MessageService {

    MessageRepository messageRepository;
    ConversationService conversationService;

    public MessageServiceImpl(MessageRepository messageRepository, ConversationService conversationService) {
        this.messageRepository = messageRepository;
        this.conversationService = conversationService;
    }

    @Override
    public Message createMessage(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public Message getMessage(String id) {
        return messageRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new NotFoundException("Message not found"));
    }

    @Override
    public List<Message> getMessagesInConversationBetweenTimes(UUID conversationId, OffsetDateTime startTime,
            OffsetDateTime endTime) {
        return this.messageRepository.findByConversationIdAndCreatedAtBetween(conversationId, startTime, endTime);
    }

    @Override
    public List<Message> searchMessages(UUID conversationId, String searchString) {
        return this.messageRepository.searchAndRankByFullText(conversationId, searchString);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canUserAccessMessage(UUID userId, String conversationId) {
        return this.conversationService.isUserInConversation(UUID.fromString(conversationId), userId);
    }
}
