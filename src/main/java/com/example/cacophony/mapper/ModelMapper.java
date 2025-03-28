package com.example.cacophony.mapper;

import com.example.cacophony.data.dto.*;
import com.example.cacophony.data.model.*;
import com.example.cacophony.service.UserService;
import com.example.cacophony.util.Identity;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ModelMapper {
        
    UserService userService;
    public ModelMapper(UserService userService) {
        this.userService = userService;
    }

    public User requestToUser(CreateUserRequest createUserRequest) {
        return User.builder()
                .email(createUserRequest.getEmail())
                .password(createUserRequest.getPassword())
                .username(createUserRequest.getUsername())
                .createdAt(OffsetDateTime.now())
                .build();
    }

    public CreateUserResponse userToResponse(User user) {
        return CreateUserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .userName(user.getUsername())
                .build();
    }

    public Chat requestToChat(CreateChatRequest createChatRequest) {
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

    public CreateChatResponse chatToCreateResponse(Chat chat) {
        return CreateChatResponse.builder()
                .id(chat.getConversation().getId())
                .name(chat.getName())
                .description(chat.getDescription())
                .members(chat.getConversation().getMembers().stream().map(User::getId).toList())
                .build();
    }

    public Message createMessageRequestToMessage(CreateMessageRequest createMessageRequest) {
        return Message.builder()
                .text(createMessageRequest.getMessage())
                .user(userService.getUserFromId(UUID.fromString(Identity.getUserId())))
                .conversation(
                        Conversation.builder()
                                .id(createMessageRequest.getConversationId())
                                .type(ConversationType.CHAT)
                                .build()
                        )
                .build();
    }

    public MessageResponse messageToResponse(Message message) {
        return MessageResponse.builder()
                .id(message.getId())
                .userId(message.getUser().getId())
                .message(message.getText())
                .conversationId(message.getConversation().getId())
                .createdAt(message.getCreatedAt())
                .updatedAt(message.getUpdatedAt())
                .build();
    }

    public Channel requestToChannel(CreateChannelRequest request) {
        return Channel.builder()
                .description(request.getDescription())
                .name(request.getName())
                .conversation(Conversation.builder()
                        .type(ConversationType.CHANNEL)
                        .members(request.getMembers().stream().map(User::fromId).toList())
                        .build()
                )
                .build();
    }

    public CreateChannelResponse channelToCreateResponse(Channel channel) {
        return CreateChannelResponse.builder()
                .id(channel.getConversation().getId())
                .name(channel.getName())
                .description(channel.getDescription())
                .members(channel.getConversation().getMembers().stream().map(User::getId).toList())
        .build();
    }

    public List<MessageResponse> listOfMessagesToRespones(List<Message> messageList) {
        List<MessageResponse> messageResponseList = new ArrayList();
        for(Message message: messageList) {
            messageResponseList.add(messageToResponse(message));
        }
        return messageResponseList;
    }

}