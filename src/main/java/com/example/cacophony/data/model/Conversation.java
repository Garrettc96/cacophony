package com.example.cacophony.data.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Converter(autoApply = true)
class ConversationTypeConverter implements AttributeConverter<ConversationType, String> {

    @Override
    public String convertToDatabaseColumn(ConversationType category) {
        if (category == null) {
            return null;
        }
        return category.name();
    }

    @Override
    public ConversationType convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }

        return Stream.of(ConversationType.values()).filter(c -> c.name().equals(code)).findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @CreatedDate
    OffsetDateTime createdAt;
    @LastModifiedDate
    OffsetDateTime updatedAt;

    ConversationType type;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    List<User> members;

    public static Conversation of(UUID id, ConversationType type) {
        return Conversation.builder().id(id).type(type).build();
    }

    public static Conversation of(ConversationType type) {
        return Conversation.builder().type(type).build();
    }

}
