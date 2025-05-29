package com.example.cacophony.repository;

import org.jooq.DSLContext;
import org.jooq.Record5;
import org.jooq.Record6;
import org.jooq.Result;

import com.example.cacophony.data.model.Conversation;
import com.example.cacophony.data.model.ConversationWithMembers;
import com.example.cacophony.jooq.tables.records.ConversationRecord;

import static com.example.cacophony.jooq.tables.UserConversation.USER_CONVERSATION;
import com.example.cacophony.jooq.tables.records.UserConversationRecord;
import static com.example.cacophony.jooq.tables.Conversation.CONVERSATION;
import static com.example.cacophony.util.Jooq.optional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;;

public class ConversationJooqRepository {

    DSLContext dsl;

    public ConversationJooqRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public ConversationRecord createConversation(ConversationRecord conversation) {
        return dsl.insertInto(CONVERSATION).columns(CONVERSATION.TYPE).values(conversation.getType()).returning()
                .fetchOne();
    }

    public List<UserConversationRecord> addUsersToConversation(List<UUID> users, UUID conversationId) {
        List<UserConversationRecord> results = users.stream()
                .map(user -> new UserConversationRecord(user, conversationId)).toList();
        dsl.batchInsert(results).execute();
        return results;
    }

    public boolean existsByIdAndMembersContains(UUID id, UUID userId) {
        return dsl.select(USER_CONVERSATION).from(USER_CONVERSATION)
                .where(USER_CONVERSATION.CHANNEL_ID.eq(id).and(USER_CONVERSATION.USER_ID.eq(userId)))
                .fetchOne() != null;
    }

    public Optional<ConversationRecord> findById(UUID conversationId) {
        return dsl.selectFrom(CONVERSATION).where(CONVERSATION.ID.eq(conversationId)).fetchOptional();
    }

    public ConversationWithMembers getConversationWithMembers(UUID conversationId) {
        Result<Record6<UUID, String, UUID, OffsetDateTime, OffsetDateTime, String>> result = dsl
                .select(USER_CONVERSATION.conversation().ID, USER_CONVERSATION.conversation().TYPE,
                        USER_CONVERSATION.USER_ID, USER_CONVERSATION.conversation().CREATED_AT,
                        USER_CONVERSATION.conversation().UPDATED_AT, USER_CONVERSATION.conversation().TYPE)
                .from(USER_CONVERSATION).where(USER_CONVERSATION.CHANNEL_ID.eq(conversationId)).fetch();
        List<UUID> members = result.stream().map(Record6::value3).toList();
        return new ConversationWithMembers(new ConversationRecord(result.get(0).value1(), result.get(0).value2(),
                result.get(0).value4(), result.get(0).value5()), members);

    }
}
