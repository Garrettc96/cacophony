package com.example.cacophony.service;

import com.example.cacophony.data.model.Chat;
import com.example.cacophony.data.model.ChatWithMembers;
import com.example.cacophony.jooq.tables.records.ChatRecord;
import com.example.cacophony.repository.ChatJooqRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface ChatService {
    public ChatRecord createChat(final ChatRecord chat, List<UUID> members);

    public ChatRecord getChat(String chatId);

    public ChatWithMembers getChatWithMembers(String chatId);

    public boolean canUserAccessChat(UUID userId, String chatId);
}
