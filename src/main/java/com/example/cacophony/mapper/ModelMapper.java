package com.example.cacophony.mapper;

import com.example.cacophony.data.dto.*;
import com.example.cacophony.data.model.*;

import java.time.OffsetDateTime;

public class ModelMapper {
    public static User requestToUser(CreateUserRequest createUserRequest) {
        return User.builder()
                .email(createUserRequest.getEmail())
                .password(createUserRequest.getPassword())
                .username(createUserRequest.getUsername())
                .createdAt(OffsetDateTime.now())
                .build();
    }

    public static CreateUserResponse userToResponse(User user) {
        return CreateUserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .userName(user.getUsername())
                .build();
    }

    public static Chat requestToChat(CreateChatRequest createChatRequest) {
        return Chat.builder()
                .name(createChatRequest.getName())
                .description(createChatRequest.getDescription())
                .conversation(Conversation.builder()
                        .type(ConversationType.CHAT)
                        .members(createChatRequest.getMembers().stream().map(User::fromId).toList())
                        .build()
                )
                .build();
    }

    public static CreateChatResponse chatToCreateResponse(Chat chat) {
        return CreateChatResponse.builder()
                .id(chat.getConversation().getId())
                .name(chat.getName())
                .description(chat.getDescription())
                .members(chat.getConversation().getMembers().stream().map(User::getUsername).toList())
                .build();
    }

    public static Message createMessageRequestToMessage(CreateMessageRequest createMessageRequest) {
        return Message.builder()
                .text(createMessageRequest.getMessage())
                .user(User.fromId(createMessageRequest.getUserId()))
                .conversation(
                        Conversation.builder()
                                .id(createMessageRequest.getConversationId())
                                .type(ConversationType.CHAT)
                                .build()
                        )
                .build();
    }

    public static MessageResponse messageToResponse(Message message) {
        return MessageResponse.builder()
                .id(message.getId())
                .userId(message.getUser().getId())
                .message(message.getText())
                .conversationId(message.getConversation().getId())
                .createdAt(message.getCreatedAt())
                .updatedAt(message.getUpdatedAt())
                .build();
    }
}
