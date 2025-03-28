package com.example.cacophony.repository;

import com.example.cacophony.data.model.Conversation;
import com.example.cacophony.data.model.ConversationId;
import com.example.cacophony.data.model.User;

import org.springframework.data.repository.ListCrudRepository;

import java.util.UUID;

public interface ConversationRepository extends ListCrudRepository<Conversation, UUID> {

    public boolean existsByIdAndMembersContains(UUID id, User user);
}
