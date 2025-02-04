package com.example.cacophony.controllers;

import com.example.cacophony.data.dto.CreateChatRequest;
import com.example.cacophony.data.dto.CreateChatResponse;
import com.example.cacophony.data.model.Chat;
import com.example.cacophony.mapper.ModelMapper;
import com.example.cacophony.service.ChatService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.example.cacophony.util.TimeUtil.epochToTimestamp;

@RestController
@RequestMapping("chats")
public class ChatController {
    private final ChatService chatService;
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public CreateChatResponse createChat(@Valid @RequestBody CreateChatRequest request) {
        return ModelMapper.chatToCreateResponse(this.chatService.createChat(ModelMapper.requestToChat(request)));
    }

    @GetMapping("/{id}")
    public Chat getChat(@PathVariable String id) {
        return this.chatService.getChat(id);
    }
}
