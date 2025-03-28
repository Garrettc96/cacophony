package com.example.cacophony.repository;

import com.example.cacophony.data.model.Chat;
import com.example.cacophony.data.model.ConversationId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface ChatRepository extends ListCrudRepository<Chat, UUID> {
    List<Chat> findByCreatedAtBetween(OffsetDateTime startTime, OffsetDateTime endTime);
}
