package com.example.cacophony.service;

import com.example.cacophony.data.model.Chat;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface ChatService {
    public Chat createChat(Chat chat);

    public Chat getChat(String chatId);

    public List<Chat> getChatsByTimestamp(OffsetDateTime startEpoch, OffsetDateTime endEpoch);

    public boolean canUserAccessChat(UUID userId, String chatId);
}
