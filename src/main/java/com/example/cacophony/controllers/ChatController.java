package com.example.cacophony.controllers;

import com.example.cacophony.data.dto.CreateChatRequest;
import com.example.cacophony.data.dto.ChatResponse;
import com.example.cacophony.data.model.Chat;
import com.example.cacophony.data.model.ChatWithMembers;
import com.example.cacophony.mapper.ModelMapper;
import com.example.cacophony.repository.ChatJooqRepository;
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
    public ResponseEntity<ChatResponse> createChat(@Valid @RequestBody CreateChatRequest request) {
        return ResponseEntity.ok(modelMapper.chatToResponse(
                this.chatService.createChat(modelMapper.requestToChat(request), request.getMembers()),
                request.getMembers()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChatResponse> getChat(@PathVariable String id) {
        ChatWithMembers result = this.chatService.getChatWithMembers(id);
        return ResponseEntity.ok(modelMapper.chatToResponse(result.chat(), result.members()));
    }
}
