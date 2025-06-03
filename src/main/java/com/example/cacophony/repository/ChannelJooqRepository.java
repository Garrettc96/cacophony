package com.example.cacophony.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.jooq.DSLContext;

import com.example.cacophony.data.model.ChannelWithMembers;
import com.example.cacophony.jooq.tables.records.ChannelRecord;
import static com.example.cacophony.jooq.tables.Channel.CHANNEL;
import static com.example.cacophony.jooq.tables.UserConversation.USER_CONVERSATION;
import static com.example.cacophony.util.Jooq.optional;

public class ChannelJooqRepository {

    DSLContext dsl;

    public ChannelJooqRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public ChannelRecord createChannel(ChannelRecord channel) {
        return this.dsl.insertInto(CHANNEL).columns(CHANNEL.ID, CHANNEL.NAME, CHANNEL.DESCRIPTION, CHANNEL.VISIBILITY)
                .values(channel.getId(), channel.getName(), channel.getDescription(), channel.getVisibility())
                .returning().fetchOne();
    }

    public Optional<ChannelRecord> getChannel(UUID channelId) {
        return this.dsl.selectFrom(CHANNEL).where(CHANNEL.ID.eq(channelId)).fetchOptional();
    }

    public List<UUID> getUsersInChannel(UUID channelId) {
        return this.dsl.select(USER_CONVERSATION.USER_ID).from(USER_CONVERSATION)
                .where(USER_CONVERSATION.CHANNEL_ID.eq(channelId)).fetch().map(r -> r.value1());
    }

    public boolean canUserAccessChannel(UUID channelId, UUID userId) {
        return this.dsl.select(USER_CONVERSATION).from(USER_CONVERSATION)
                .where(USER_CONVERSATION.CHANNEL_ID.eq(channelId).and(USER_CONVERSATION.USER_ID.eq(userId)))
                .fetchOne() == null;
    }

    public Optional<ChannelWithMembers> getChannelWithMembers(UUID channelId) {
        var result = this.dsl.select(CHANNEL.asterisk(), USER_CONVERSATION.USER_ID).from(CHANNEL)
                .leftJoin(USER_CONVERSATION).on(USER_CONVERSATION.CHANNEL_ID.eq(CHANNEL.ID))
                .where(CHANNEL.ID.eq(channelId)).fetch();

        if (result.isEmpty()) {
            return Optional.empty();
        }

        // First row contains the chat data
        ChannelRecord channel = result.get(0).into(CHANNEL);

        // Extract all user IDs from the results
        List<UUID> members = result.stream().map(r -> r.get(USER_CONVERSATION.USER_ID)).filter(id -> id != null) // Filter
                                                                                                                 // out
                                                                                                                 // null
                                                                                                                 // values
                                                                                                                 // from
                                                                                                                 // LEFT
                                                                                                                 // JOIN
                .toList();

        return Optional.of(new ChannelWithMembers(channel, members));
    }
}
