package com.example.cacophony.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.cacophony.data.dto.MessageResponse;
import com.example.cacophony.exception.NotFoundException;
import com.example.cacophony.mapper.ModelMapper;
import com.example.cacophony.service.MessageService;

import static com.example.cacophony.util.TimeUtil.epochToTimestamp;

@RestController
@RequestMapping("conversations")
public class ConversationController {

    MessageService messageService;
    private final ModelMapper modelMapper;

    public ConversationController(MessageService messageService, ModelMapper modelMapper) {
        this.messageService = messageService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/search")
    public ResponseEntity<List<MessageResponse>> searchMessages(
            @RequestParam(name = "conversationId") UUID conversationId,
            @RequestParam(name = "startEpoch") Optional<Long> startEpoch,
            @RequestParam(name = "endEpoch") Optional<Long> endEpoch,
            @RequestParam(name = "queryText") Optional<String> queryText) {
        if (startEpoch.isPresent() && endEpoch.isPresent()) {
            return ResponseEntity.of(Optional
                    .of(modelMapper.listOfMessagesToRespones(this.messageService.getMessagesInConversationBetweenTimes(
                            conversationId, epochToTimestamp(startEpoch.get()), epochToTimestamp(endEpoch.get())))));
        } else if (queryText.isPresent()) {
            return ResponseEntity.of(Optional.of(modelMapper
                    .listOfMessagesToRespones(this.messageService.searchMessages(conversationId, queryText.get()))));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
