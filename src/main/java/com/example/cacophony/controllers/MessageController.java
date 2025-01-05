package com.example.cacophony.controllers;

import com.example.cacophony.data.dto.CreateMessageRequest;
import com.example.cacophony.data.dto.MessageResponse;
import com.example.cacophony.mapper.ModelMapper;
import com.example.cacophony.service.MessageService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("messages")
public class MessageController {
    MessageService messageService;
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public MessageResponse createMessage(@RequestBody CreateMessageRequest request) {
        return ModelMapper.messageToResponse(
                this.messageService.createMessage(ModelMapper.createMessageRequestToMessage(request))
        );
    }

    @GetMapping("/{id}")
    public MessageResponse getMessage(@PathVariable String id) {
        return ModelMapper.messageToResponse(
                this.messageService.getMessage(id)
        );
    }


}
