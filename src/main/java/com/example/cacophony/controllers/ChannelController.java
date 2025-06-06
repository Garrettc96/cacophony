package com.example.cacophony.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.cacophony.data.dto.CreateChannelRequest;
import com.example.cacophony.data.dto.ChannelResponse;
import com.example.cacophony.data.model.Channel;
import com.example.cacophony.data.model.ChannelWithMembers;
import com.example.cacophony.exception.NotFoundException;
import com.example.cacophony.mapper.ModelMapper;
import com.example.cacophony.service.ChannelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import jakarta.validation.Valid;

@RestController
@RequestMapping("channels")
public class ChannelController {
    private final ChannelService channelService;
    private final ModelMapper modelMapper;

    public ChannelController(ChannelService channelService, ModelMapper modelMapper) {
        this.channelService = channelService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ChannelResponse> createChannel(@Valid @RequestBody CreateChannelRequest request) {
        return ResponseEntity.ok(modelMapper.channelToCreateResponse(
                this.channelService.createChannel(modelMapper.requestToChannel(request), request.getMembers()),
                request.getMembers()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChannelResponse> getChannel(@PathVariable String id) {
        ChannelWithMembers channel = this.channelService.getChannelWithMembers(id)
                .orElseThrow(() -> new NotFoundException(String.format("Channel with ID %s not found", id)));
        return ResponseEntity.ok(this.modelMapper.channelToCreateResponse(channel.channel(), channel.members()));
    }
}
