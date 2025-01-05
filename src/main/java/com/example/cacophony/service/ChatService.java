package com.example.cacophony.service;

import com.example.cacophony.data.model.Chat;

public interface ChatService {
    public Chat createChat(Chat chat);
    public Chat getChat(String chatId);
}
