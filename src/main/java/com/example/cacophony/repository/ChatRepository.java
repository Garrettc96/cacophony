package com.example.cacophony.repository;

import com.example.cacophony.data.model.Chat;
import com.example.cacophony.data.model.ConversationId;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ChatRepository extends CrudRepository<Chat, UUID> {}
