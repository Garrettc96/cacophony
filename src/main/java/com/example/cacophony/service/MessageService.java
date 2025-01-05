package com.example.cacophony.service;

import com.example.cacophony.data.model.Message;

public interface MessageService {
    public Message createMessage(Message message);
    public Message getMessage(String id);
}
