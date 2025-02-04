package com.example.cacophony.service;

import com.example.cacophony.data.model.Message;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface MessageService {
    public Message createMessage(Message message);
    public Message getMessage(String id);
    public List<Message> getMessagesInConversationBetweenTimes(UUID conversationId, OffsetDateTime startTime, OffsetDateTime endTime);
}
