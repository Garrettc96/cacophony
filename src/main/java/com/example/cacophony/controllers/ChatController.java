package com.example.cacophony.controllers;

import com.example.cacophony.data.dto.CreateChatRequest;
import com.example.cacophony.data.dto.CreateChatResponse;
import com.example.cacophony.data.model.Chat;
import com.example.cacophony.mapper.ModelMapper;
import com.example.cacophony.service.ChatService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("chats")
public class ChatController {
    private final ChatService chatService;
    private final ModelMapper modelMapper;
    public ChatController(ChatService chatService, ModelMapper modelMapper) {
        this.chatService = chatService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CreateChatResponse> createChat(@Valid @RequestBody CreateChatRequest request) {
        return ResponseEntity.ok(modelMapper.chatToCreateResponse(this.chatService.createChat(modelMapper.requestToChat(request))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Chat> getChat(@PathVariable String id) {
        return ResponseEntity.ok(this.chatService.getChat(id));
    }
}
