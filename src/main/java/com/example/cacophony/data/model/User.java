package com.example.cacophony.data.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "c_users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Access(AccessType.PROPERTY)
    UUID id;
    @ManyToMany
    @JoinTable(
            name = "user_conversations",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "channel_id"))
    Set<Conversation> conversations;
    @Column(unique = true)
    String username;
    String email;
    String password;
    @CreatedDate
    OffsetDateTime createdAt;
    @LastModifiedDate
    OffsetDateTime updatedAt;
    public static User fromId(UUID id) {
        return User.builder().id(id).build();
    }

    public static User fromName(String name) {
        return User.builder().username(name).build();
    }


}
