package com.example.cacophony.data.model;


import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.OffsetDateTime;
import java.util.List;

enum CHANNEL_VISIBILITY {
    PUBLIC,
    PRIVATE;
}

@Data
@Entity
public class Channel {
    @Id
    private String id;

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

}
