package com.example.cacophony.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.cacophony.data.model.Channel;
import com.example.cacophony.data.model.ChannelWithMembers;
import com.example.cacophony.data.model.Conversation;
import com.example.cacophony.data.model.ConversationType;
import com.example.cacophony.data.model.User;
import com.example.cacophony.exception.NotFoundException;
import com.example.cacophony.jooq.tables.records.ChannelRecord;
import com.example.cacophony.jooq.tables.records.ConversationRecord;
import com.example.cacophony.repository.ChannelJooqRepository;
import com.example.cacophony.repository.ChannelRepository;
import com.example.cacophony.repository.ConversationJooqRepository;
import com.example.cacophony.repository.ConversationRepository;

public class ChannelServiceImpl implements ChannelService {

    ChannelJooqRepository channelRepository;
    ConversationService conversationService;
    UserService userService;

    public ChannelServiceImpl(ChannelJooqRepository channelRepository, ConversationService conversationService,
            UserService userService) {
        this.channelRepository = channelRepository;
        this.conversationService = conversationService;
        this.userService = userService;
    }

    @Override
    public ChannelRecord createChannel(ChannelRecord channel, List<UUID> members) {
        ConversationRecord conversation = this.conversationService.createConversation(channelToConversation(channel),
                members);
        ChannelRecord channelWithConversation = new ChannelRecord(conversation.getId(), channel.getName(),
                channel.getDescription(), channel.getVisibility(), channel.getCreatedAt(), channel.getUpdatedAt());
        return channelRepository.createChannel(channelWithConversation);
    }

    @Override
    public ChannelRecord getChannel(String channelId) {
        // TODO Auto-generated method stub
        return channelRepository.getChannel(UUID.fromString(channelId))
                .orElseThrow(() -> new NotFoundException("Channel not found"));
    }

    @Override
    public Optional<ChannelWithMembers> getChannelWithMembers(String channelId) {
        return channelRepository.getChannelWithMembers(UUID.fromString(channelId));
    }

    private ConversationRecord channelToConversation(ChannelRecord channel) {
        return new ConversationRecord(null, ConversationType.CHANNEL.name(), null, null);
    }
}
