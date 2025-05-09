package com.example.cacophony.data.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    private User user;
    private String text;
    @ManyToOne
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;
    @Column(name = "fts_vector", columnDefinition = "tsvector")
    @Basic(fetch = FetchType.LAZY) // Avoid fetching it unless needed
    @ColumnTransformer(write = "?::tsvector")
    private String ftsVector;
    @Column(name = "created_at")
    @CreationTimestamp
    private OffsetDateTime createdAt;
    @Column(name = "updated_at")
    @UpdateTimestamp
    private OffsetDateTime updatedAt;
}
