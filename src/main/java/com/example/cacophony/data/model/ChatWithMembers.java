package com.example.cacophony.data.model;

import java.util.List;
import java.util.UUID;

import com.example.cacophony.jooq.tables.records.ChatRecord;

public record ChatWithMembers(ChatRecord chat, List<UUID> members) {
}
