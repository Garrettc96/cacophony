package com.example.cacophony.controllers;

import com.example.cacophony.data.dto.CreateMessageRequest;
import com.example.cacophony.data.dto.CreateMessageReactRequest;
import com.example.cacophony.data.dto.ImageUploadResponse;
import com.example.cacophony.data.dto.MessageResponse;
import com.example.cacophony.data.dto.ImageUploadDetails;
import com.example.cacophony.data.model.Chat;
import com.example.cacophony.data.model.Message;
import com.example.cacophony.mapper.ModelMapper;
import com.example.cacophony.service.MessageServiceImpl;
import com.example.cacophony.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
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
                this.messageService.createMessage(modelMapper.createMessageRequestToMessage(request))));
    }

    @PostMapping("/{messageId}/react")
    public ResponseEntity<Void> reactToMessage(
            @PathVariable UUID messageId,
            @RequestBody CreateMessageReactRequest request) {
        this.messageService.addReactionToMessage(messageId, request.getReactId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{messageId}/react/{reactId}")
    public ResponseEntity<Void> removeReactionFromMessage(
            @PathVariable UUID messageId,
            @PathVariable UUID reactId) {
        this.messageService.removeReactionFromMessage(messageId, reactId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{conversationId}/uploadImage")
    public ResponseEntity<ImageUploadResponse> uploadImage(@PathVariable Optional<String> conversationId) {
        ImageUploadDetails details = this.messageService.generateImageUploadUrl(conversationId.get());
        return ResponseEntity
                .ok(modelMapper.createImpageUploadResponse(conversationId.get(), details.s3Path(), details.url()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MessageResponse> getMessage(@PathVariable String id) {
        return ResponseEntity.ok(modelMapper.messageToResponse(this.messageService.getMessage(id)));
    }
}
