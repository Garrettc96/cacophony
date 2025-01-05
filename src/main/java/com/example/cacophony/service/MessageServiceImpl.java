package com.example.cacophony.service;

import com.example.cacophony.data.model.Message;
import com.example.cacophony.exception.NotFoundException;
import com.example.cacophony.repository.MessageRepository;

import java.util.UUID;

public class MessageServiceImpl implements MessageService {

    MessageRepository messageRepository;
    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Message createMessage(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public Message getMessage(String id) {
        return messageRepository.findById(UUID.fromString(id)).orElseThrow(() -> new NotFoundException("Message not found"));
    }
}
