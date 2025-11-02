package com.example.cacophony.repository;

import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.impl.DSL;
import static com.example.cacophony.jooq.tables.Message.MESSAGE;
import static com.example.cacophony.jooq.tables.MessageReact.MESSAGE_REACT;
import static com.example.cacophony.util.Jooq.optional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.cacophony.jooq.tables.records.MessageRecord;
import com.example.cacophony.jooq.tables.Message;
import org.jooq.Field;
import org.springframework.stereotype.Repository;

@Repository
public class MessageJooqRepository {

    private final DSLContext dsl;

    public MessageJooqRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public MessageRecord createMessage(MessageRecord message) {
        return this.dsl.insertInto(MESSAGE)
                .columns(MESSAGE.USER_ID, MESSAGE.CONVERSATION_ID, MESSAGE.TEXT, MESSAGE.S3_PATH)
                .values(message.getUserId(), message.getConversationId(), message.getText(), message.getS3Path())
                .returning().fetchOne();
    }

    public Optional<MessageRecord> findById(UUID id) {
        return this.dsl.selectFrom(MESSAGE).where(MESSAGE.ID.eq(id)).fetchOptional();
    }

    public List<MessageRecord> findByConversationIdAndCreatedAtBetween(UUID conversationId, OffsetDateTime startTime,
            OffsetDateTime endTime) {
        return this.dsl.selectFrom(MESSAGE)
                .where(MESSAGE.CONVERSATION_ID.eq(conversationId).and(MESSAGE.CREATED_AT.between(startTime, endTime)))
                .fetch();
    }

    public List<MessageRecord> searchMessages(UUID conversationId, String query) {
        return dsl.resultQuery("""
                SELECT m.*, ts_rank_cd(m.fts_vector, plainto_tsquery({1})) as rank
                FROM message m
                WHERE conversation_id = {0}
                AND m.fts_vector @@ plainto_tsquery({1})
                ORDER BY rank DESC
                """, conversationId, query).fetchInto(MessageRecord.class);
    }

    public void addReactionToMessage(UUID messageId, UUID reactId, UUID userId) {
        dsl.insertInto(MESSAGE_REACT).set(MESSAGE_REACT.MESSAGE_ID, messageId).set(MESSAGE_REACT.REACT_ID, reactId)
                .set(MESSAGE_REACT.USER_ID, userId).execute();
    }

    public void removeReactionFromMessage(UUID messageId, UUID reactId, UUID userId) {
        dsl.deleteFrom(MESSAGE_REACT).where(MESSAGE_REACT.MESSAGE_ID.eq(messageId))
                .and(MESSAGE_REACT.REACT_ID.eq(reactId)).and(MESSAGE_REACT.USER_ID.eq(userId)).execute();
    }

}
