package com.example.cacophony.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.cacophony.data.model.Channel;
import com.example.cacophony.data.model.ChannelWithMembers;
import com.example.cacophony.jooq.tables.records.ChannelRecord;

public interface ChannelService {
    public ChannelRecord createChannel(ChannelRecord channel, List<UUID> members);

    public ChannelRecord getChannel(String channelId);

    public Optional<ChannelWithMembers> getChannelWithMembers(String channelId);
}
