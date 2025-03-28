package com.example.cacophony.data.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

enum CHANNEL_VISIBILITY {
    PUBLIC,
    PRIVATE;
}

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Channel implements Serializable {
    @Id
    private UUID id;

    @MapsId
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id")
    private Conversation conversation;
    private String name;
    private String description;
    private CHANNEL_VISIBILITY visibility;
    @CreatedDate
    private OffsetDateTime createdAt;
    @LastModifiedDate
    private OffsetDateTime updatedAt;

    public static Channel of(Channel channel, Conversation conversation) {
        return Channel.builder()
                .conversation(conversation)
                .description(channel.getDescription())
                .name(channel.getName())
                .build();
    }
}


