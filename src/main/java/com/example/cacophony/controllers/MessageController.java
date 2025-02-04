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

import static com.example.cacophony.util.TimeUtil.epochToTimestamp;

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

    @GetMapping("/search")
    public ResponseEntity<List<MessageResponse>> searchMessages(
        @RequestParam(name="conversationId") UUID conversationId,
        @RequestParam(name = "startEpoch") long startEpoch,
        @RequestParam(name = "endEpoch") long endEpoch) {
        return ResponseEntity.of(
            Optional.of(
                this.messageService.getMessagesInConversationBetweenTimes(conversationId, epochToTimestamp(startEpoch), epochToTimestamp(endEpoch)).stream()
                    .map(ModelMapper::messageToResponse).toList()
            )
        );
    }

}
