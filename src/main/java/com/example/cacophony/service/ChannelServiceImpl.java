package com.example.cacophony.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;


import com.example.cacophony.data.model.Channel;
import com.example.cacophony.data.model.Conversation;
import com.example.cacophony.data.model.User;
import com.example.cacophony.exception.NotFoundException;
import com.example.cacophony.repository.ChannelRepository;
import com.example.cacophony.repository.ConversationRepository;

public class ChannelServiceImpl implements ChannelService {

    ChannelRepository channelRepository;
    ConversationRepository conversationRepository;
    UserService userService;
    
    public ChannelServiceImpl(ChannelRepository channelRepository, ConversationRepository conversationRepository ,UserService userService) {
        this.channelRepository = channelRepository;
        this.conversationRepository = conversationRepository;
        this.userService = userService;
    }

    @Override
    public Channel createChannel(Channel channel) {
        // TODO Auto-generated method stub
        List<User> chatMembers = this.userService.validateUsers(channel.getConversation().getMembers());
        Conversation conversation = this.conversationRepository.save(
            Conversation.builder()
                .members(chatMembers)
                .type(channel.getConversation().getType())
                .build()
        );
        return channelRepository.save(Channel.of(channel, conversation));
    }

    @Override
    public Channel getChannel(String channelId) {
        // TODO Auto-generated method stub
        return this.channelRepository.findById(UUID.fromString(channelId)).orElseThrow(() ->
                new NotFoundException(String.format("Channel %s not found", channelId)));
    }
}
