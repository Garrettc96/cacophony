package com.example.cacophony.service;

import com.example.cacophony.data.dto.ImageUploadDetails;
import com.example.cacophony.data.model.Message;
import com.example.cacophony.jooq.tables.records.MessageRecord;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface MessageService {
    public MessageRecord createMessage(MessageRecord message);

    public MessageRecord getMessage(String id);

    public List<MessageRecord> getMessagesInConversationBetweenTimes(UUID conversationId, OffsetDateTime startTime,
            OffsetDateTime endTime);

    public List<MessageRecord> searchMessages(UUID conversationId, String searchString);

    public boolean canUserAccessMessage(UUID userId, String messageId);

    public ImageUploadDetails generateImageUploadUrl(String conversationId);

    void addReactionToMessage(UUID messageId, UUID reactId);

    void removeReactionFromMessage(UUID messageId, UUID reactId);
}
