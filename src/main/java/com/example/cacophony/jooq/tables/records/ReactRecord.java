/*
 * This file is generated by jOOQ.
 */
package com.example.cacophony.jooq.tables.records;

import com.example.cacophony.jooq.tables.React;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.jooq.Record1;
import org.jooq.impl.UpdatableRecordImpl;

/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class ReactRecord extends UpdatableRecordImpl<ReactRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>cacophony.react.id</code>.
     */
    public void setId(UUID value) {
        set(0, value);
    }

    /**
     * Getter for <code>cacophony.react.id</code>.
     */
    public UUID getId() {
        return (UUID) get(0);
    }

    /**
     * Setter for <code>cacophony.react.user_id</code>.
     */
    public void setUserId(UUID value) {
        set(1, value);
    }

    /**
     * Getter for <code>cacophony.react.user_id</code>.
     */
    public UUID getUserId() {
        return (UUID) get(1);
    }

    /**
     * Setter for <code>cacophony.react.name</code>.
     */
    public void setName(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>cacophony.react.name</code>.
     */
    public String getName() {
        return (String) get(2);
    }

    /**
     * Setter for <code>cacophony.react.s3_path</code>.
     */
    public void setS3Path(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>cacophony.react.s3_path</code>.
     */
    public String getS3Path() {
        return (String) get(3);
    }

    /**
     * Setter for <code>cacophony.react.created_at</code>.
     */
    public void setCreatedAt(OffsetDateTime value) {
        set(4, value);
    }

    /**
     * Getter for <code>cacophony.react.created_at</code>.
     */
    public OffsetDateTime getCreatedAt() {
        return (OffsetDateTime) get(4);
    }

    /**
     * Setter for <code>cacophony.react.updated_at</code>.
     */
    public void setUpdatedAt(OffsetDateTime value) {
        set(5, value);
    }

    /**
     * Getter for <code>cacophony.react.updated_at</code>.
     */
    public OffsetDateTime getUpdatedAt() {
        return (OffsetDateTime) get(5);
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
     * Create a detached ReactRecord
     */
    public ReactRecord() {
        super(React.REACT);
    }

    /**
     * Create a detached, initialised ReactRecord
     */
    public ReactRecord(UUID id, UUID userId, String name, String s3Path, OffsetDateTime createdAt,
            OffsetDateTime updatedAt) {
        super(React.REACT);

        setId(id);
        setUserId(userId);
        setName(name);
        setS3Path(s3Path);
        setCreatedAt(createdAt);
        setUpdatedAt(updatedAt);
        resetTouchedOnNotNull();
    }
}
