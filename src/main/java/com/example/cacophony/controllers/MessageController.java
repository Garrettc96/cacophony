package com.example.cacophony.controllers;

import com.example.cacophony.data.dto.CreateMessageRequest;
import com.example.cacophony.data.dto.MessageResponse;
import com.example.cacophony.data.model.Chat;
import com.example.cacophony.data.model.Message;
import com.example.cacophony.mapper.ModelMapper;
import com.example.cacophony.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("messages")
public class MessageController {
    MessageService messageService;
    ModelMapper modelMapper;
    public MessageController(MessageService messageService, ModelMapper modelMapper) {
        this.messageService = messageService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<MessageResponse> createMessage(@RequestBody CreateMessageRequest request) {
        return ResponseEntity.ok(modelMapper.messageToResponse(
                this.messageService.createMessage(modelMapper.createMessageRequestToMessage(request))
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MessageResponse> getMessage(@PathVariable String id) {
        return ResponseEntity.ok(modelMapper.messageToResponse(
                this.messageService.getMessage(id)
        ));
    }

}
