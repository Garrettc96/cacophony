package com.example.cacophony.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import com.example.cacophony.data.model.Channel;

public interface ChannelService {
    public Channel createChannel(Channel Channel);
    public Channel getChannel(String channelId);
    public List<Channel> getChannelsByTimestamp(OffsetDateTime startEpoch, OffsetDateTime endEpoch);
    public boolean canUserAccessChannel(UUID userId, String channelId);
}
