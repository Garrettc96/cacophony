package com.example.cacophony.data.model;

import java.util.List;
import java.util.UUID;

import com.example.cacophony.jooq.tables.records.ConversationRecord;

public record ConversationWithMembers(ConversationRecord conversation, List<UUID> members) {

}
