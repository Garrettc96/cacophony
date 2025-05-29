package com.example.cacophony.repository;

import org.jooq.DSLContext;

import com.example.cacophony.jooq.tables.records.CUserRecord;
import com.example.cacophony.jooq.tables.records.ChatRecord;
import com.example.cacophony.data.model.ChatWithMembers;
import static com.example.cacophony.jooq.tables.Chat.CHAT;
import static com.example.cacophony.jooq.tables.UserConversation.USER_CONVERSATION;
import static com.example.cacophony.util.Jooq.optional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class ChatJooqRepository {

    DSLContext dsl;

    public ChatJooqRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public ChatRecord createChat(ChatRecord chat) {
        return this.dsl.insertInto(CHAT).columns(CHAT.ID, CHAT.NAME, CHAT.DESCRIPTION)
                .values(chat.getId(), chat.getName(), chat.getDescription()).returning().fetchOne();
    }

    public Optional<ChatRecord> getChat(UUID chatId) {
        return this.dsl.selectFrom(CHAT).where(CHAT.ID.eq(chatId)).fetchOptional();
    }

    public ChatWithMembers getChatWithMembers(UUID chatId) {
        var result = this.dsl.select(CHAT.asterisk(), USER_CONVERSATION.USER_ID).from(CHAT).leftJoin(USER_CONVERSATION)
                .on(USER_CONVERSATION.CHANNEL_ID.eq(CHAT.ID)).where(CHAT.ID.eq(chatId)).fetch();

        if (result.isEmpty()) {
            return null;
        }

        // First row contains the chat data
        ChatRecord chat = result.get(0).into(CHAT);

        // Extract all user IDs from the results
        List<UUID> members = result.stream().map(r -> r.get(USER_CONVERSATION.USER_ID)).filter(id -> id != null) // Filter
                                                                                                                 // out
                                                                                                                 // null
                                                                                                                 // values
                                                                                                                 // from
                                                                                                                 // LEFT
                                                                                                                 // JOIN
                .toList();

        return new ChatWithMembers(chat, members);
    }

    public List<UUID> getUsersInChat(UUID chatId) {
        return this.dsl.select(USER_CONVERSATION.USER_ID).from(USER_CONVERSATION)
                .where(USER_CONVERSATION.CHANNEL_ID.eq(chatId)).fetch().map(r -> r.value1());
    }

    public boolean canUserAccessChat(UUID chatId, UUID userId) {
        return this.dsl.selectFrom(USER_CONVERSATION)
                .where(USER_CONVERSATION.CHANNEL_ID.eq(chatId).and(USER_CONVERSATION.USER_ID.eq(userId)))
                .fetchOne() == null;
    }
}
