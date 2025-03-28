package com.example.cacophony.repository;

import com.example.cacophony.data.model.Message;
import com.example.cacophony.data.model.User;
import org.springframework.data.repository.ListCrudRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface MessageRepository extends ListCrudRepository<Message, UUID> {
    public List<Message> findByConversationIdAndCreatedAtBetween(UUID id, OffsetDateTime startTime,
            OffsetDateTime endTime);
}
