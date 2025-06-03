package com.example.cacophony.data.model;

import java.util.List;
import java.util.UUID;

import com.example.cacophony.jooq.tables.records.ChannelRecord;

public record ChannelWithMembers(ChannelRecord channel, List<UUID> members) {

}
