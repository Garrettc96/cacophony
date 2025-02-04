package com.example.cacophony.service;

import com.example.cacophony.data.model.Chat;

import java.time.OffsetDateTime;
import java.util.List;

public interface ChatService {
    public Chat createChat(Chat chat);
    public Chat getChat(String chatId);
    public List<Chat> getChatsByTimestamp(OffsetDateTime startEpoch, OffsetDateTime endEpoch);
}
