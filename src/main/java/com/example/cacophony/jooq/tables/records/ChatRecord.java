/*
 * This file is generated by jOOQ.
 */
package com.example.cacophony.jooq.tables.records;

import com.example.cacophony.jooq.tables.Chat;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.jooq.Record1;
import org.jooq.impl.UpdatableRecordImpl;

/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class ChatRecord extends UpdatableRecordImpl<ChatRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>cacophony.chat.id</code>.
     */
    public void setId(UUID value) {
        set(0, value);
    }

    /**
     * Getter for <code>cacophony.chat.id</code>.
     */
    public UUID getId() {
        return (UUID) get(0);
    }

    /**
     * Setter for <code>cacophony.chat.name</code>.
     */
    public void setName(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>cacophony.chat.name</code>.
     */
    public String getName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>cacophony.chat.description</code>.
     */
    public void setDescription(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>cacophony.chat.description</code>.
     */
    public String getDescription() {
        return (String) get(2);
    }

    /**
     * Setter for <code>cacophony.chat.created_at</code>.
     */
    public void setCreatedAt(OffsetDateTime value) {
        set(3, value);
    }

    /**
     * Getter for <code>cacophony.chat.created_at</code>.
     */
    public OffsetDateTime getCreatedAt() {
        return (OffsetDateTime) get(3);
    }

    /**
     * Setter for <code>cacophony.chat.updated_at</code>.
     */
    public void setUpdatedAt(OffsetDateTime value) {
        set(4, value);
    }

    /**
     * Getter for <code>cacophony.chat.updated_at</code>.
     */
    public OffsetDateTime getUpdatedAt() {
        return (OffsetDateTime) get(4);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<UUID> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached ChatRecord
     */
    public ChatRecord() {
        super(Chat.CHAT);
    }

    /**
     * Create a detached, initialised ChatRecord
     */
    public ChatRecord(UUID id, String name, String description, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        super(Chat.CHAT);

        setId(id);
        setName(name);
        setDescription(description);
        setCreatedAt(createdAt);
        setUpdatedAt(updatedAt);
        resetTouchedOnNotNull();
    }
}
